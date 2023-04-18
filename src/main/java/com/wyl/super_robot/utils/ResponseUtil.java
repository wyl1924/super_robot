package com.wyl.super_robot.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;


public class ResponseUtil {
    public static ObjectMapper objectMapper = new ObjectMapper();

    public static void responseOutObj(Object obj) {
        if (getResponse() == null) {
            return;
        }
        getResponse().setContentType("application/json;charset=UTF-8");
        try {
            getResponse().getWriter().write(objectMapper.writeValueAsString(obj));
            getResponse().getWriter().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void responseOutObjNoClose(Object obj) {
        if (getResponse() == null) {
            return;
        }
        getResponse().setContentType("application/json;charset=UTF-8");
        getResponse().setStatus(200);
        try {
            getResponse().getWriter().write(objectMapper.writeValueAsString(obj));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HttpServletResponse getResponse() {
        try {
            return ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getResponse();
        } catch (Exception e) {
            return null;
        }

    }

    public static void outFile(String address, String fileName) {
        if (getResponse() == null) {
            return;
        }
        OutputStream out = null;
        FileInputStream fileInputStream = null;
        try {

            HttpServletResponse response = getResponse();
            response.reset();
            response.setContentType("application/octet-stream");
            response.setContentLengthLong(new File(address).length());
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "utf-8"));
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            out = response.getOutputStream();

            fileInputStream = new FileInputStream(address);
            byte[] bytes = new byte[2048];
            int len;
            while ((len = fileInputStream.read(bytes)) > 0) {
                out.write(bytes, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    @SuppressWarnings({"resource", "rawtypes"})
    public static ResponseEntity outExcel(String filename, String title, Class listClass, List list) {
        if (getResponse() == null) {
            return null;
        }
        HttpServletResponse  response = getResponse();
        response.reset();
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "utf-8"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            if (list == null || list.size() == 0) {
                HSSFWorkbook wb = new HSSFWorkbook();
                wb.createSheet();
                wb.write(out);
            } else {
                Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(title, null), listClass, list);
                HttpServletRequest request =
                        ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
                String agent = request.getHeader("USER-AGENT").toLowerCase();
                String codedFileName = URLEncoder.encode(filename, "UTF-8");
                response.setHeader("Pragma", "public");
                response.setHeader("Cache-Control", "public");
                response.setHeader("content-Type", "application/vnd.ms-excel");
                //兼容浏览器,防止导出文件名中文乱码
                if (agent.contains("firefox")) {
                    response.setCharacterEncoding("utf-8");
                    response.setHeader("content-disposition", "attachment;filename=" + new String(filename.getBytes(), "ISO8859-1") + ".xls" );
                } else {
                    response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
                }
                workbook.write(response.getOutputStream());
            }
            return new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.CREATED);

        } catch (Exception e) {

        }
        return null;
    }

    public static void responseOutObj(Object obj, int status) {
        if (getResponse() == null) {
            return;
        }
        getResponse().setContentType("application/json;charset=UTF-8");
        getResponse().setStatus(status);
        try {
            getResponse().getWriter().write(objectMapper.writeValueAsString(obj));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void outStringFile(String result, String fileName, String type) {
        ServletOutputStream out = null;
        try {
            HttpServletResponse response = getResponse();
            response.reset();
            response.setContentType("application/octet-stream");
            if (type != null) {
                response.setContentType(type);
            }
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "utf-8"));
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");

            out = response.getOutputStream();

            out.write(result.getBytes());
        } catch (Exception e) {
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
