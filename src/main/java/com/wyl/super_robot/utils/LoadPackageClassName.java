package com.wyl.super_robot.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoadPackageClassName implements ResourceLoaderAware {
    /**
     * Spring容器注入
     */
    private static ResourceLoader resourceLoader;

    public static List<String> getClassName(String packageName) {
        List<String> names = new ArrayList<String>();
        try {
            ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
            MetadataReaderFactory metaReader = new CachingMetadataReaderFactory(resourceLoader);
            Resource[] resources = resolver.getResources("classpath*:" + packageName + "/*.class");

            for (Resource r : resources) {
                MetadataReader reader;

                reader = metaReader.getMetadataReader(r);
                String[] namestr = reader.getClassMetadata().getClassName().split("\\.");
                names.add(namestr[namestr.length - 1]);
            }
        } catch (IOException e) {
            log.error(e.toString());
            log.error("获取sockert实现失败", e);
        }
        return names;
    }

    @Override
    @SuppressWarnings("static-access")
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

}
