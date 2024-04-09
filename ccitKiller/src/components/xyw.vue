<template>
  <div>
      <a-descriptions title="使用说明" layout="vertical" bordered>
    <a-descriptions-item label="Product">
      校园网自动连接工具
    </a-descriptions-item>
    <a-descriptions-item label="Programme Language">
      易语言
    </a-descriptions-item>
    <a-descriptions-item label="Author">
      王权霸业
    </a-descriptions-item>

    <a-descriptions-item label="Status" :span="3">
      <a-badge status="processing" text="Running" />
    </a-descriptions-item>

    <a-descriptions-item label="软件下载(关闭杀毒)" >
      <a-button type="primary">立即下载</a-button>
    </a-descriptions-item>

    
    <a-descriptions-item label="Config Info">
      1、因易语言开发的程序误报严重，要关闭杀毒或给软件添加信任
      <br />
      2、要允许软件添加开机启动项，方可实现开机软件自动启动，24小时自动连接校园网
      <br />
      3、由于电信网需要验证码，需要特殊手段，上手较复杂，目前电信版本仅本人自己在内测使用 <br> 所以本软件不支持电信，仅支持移动和联通
      <br />
      Package: vip.wqby.ccit.xyw
      <br />
      Storage space: 1 MB
      <br />
      Region: East China 1<br />
    </a-descriptions-item>
  </a-descriptions>
  </div>
</template>

<script>
export default {

}
</script>

<style>

</style>