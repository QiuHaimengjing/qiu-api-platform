export const openApi = [
  {
    requestLibPath: "import request from '@/utils/request'", // 想怎么引入封装请求方法
    schemaPath: 'http://localhost:8081/v2/api-docs?group=%E9%BB%98%E8%AE%A4%E5%88%86%E7%BB%84', // openAPI规范地址
    projectName: 'qiuapi-backend', // 生成到哪个目录内
    serversPath: './src/services' // 生成代码到哪个目录
  }
]
