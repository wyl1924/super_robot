<template>
  <div class="home">
    <el-container height="100%">
      <!--<el-aside width="100px" v-show="asideStatus">
        <Nav></Nav>
      </el-aside>-->
      <el-main style="padding-bottom:0px">
        <router-view></router-view>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import Nav from "../components/Nav.vue";
export default {
  name: "App",
  components: {
    Nav,
  },
  data() {
    return {
      asideStatus: true,
      firstSize: true,
      width: 0
    };
  },
  created() {
    window.addEventListener('resize', this.handleResize)
    this.handleResize()
  },

  destoryed() {
    window.removeEventListener('resize', this.handleResize)
  },
  methods: {
    resize(){
      if (window.innerWidth <= 1150) {
        this.asideStatus=false
      } else {
        this.asideStatus=true
      }
    },
   //监听窗口尺寸的变化
    handleResize() {
      if (this.firstSize){
        this.resize();
        this.firstSize = false;
        this.width = window.innerWidth;
      }
      if ( this.width != window.innerWidth ){
        this.resize();
        this.width = window.innerWidth;
      }

    }
  }
};
</script>

<style lang="scss" scoped>
.home {
  width: 100vw;
  /*height: 100vh;*/
  background-color: rgb(39, 42, 55);
  border-radius: 15px;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
}
</style>
