<script setup lang="ts"></script>

<template>
  <div class="footer">
    <div class="blog">
      <span>
        <a-link :hoverable="false" href="https://blog.invictusqiu.top" target="_blank"
          >我的博客</a-link
        >
      </span>
      <a-divider direction="vertical" />
      <span>
        <a-link :hoverable="false" href="https://github.com/QiuHaimengjing" target="_blank"
          >GitHub</a-link
        ><i class="iconfont icon-github"></i>
      </span>
    </div>
  </div>
</template>

<style scoped lang="less">
.footer {
  display: flex;
  margin: 0.5rem 0;
  justify-content: center;
  bottom: 0;
  width: 100%;
}
</style>
