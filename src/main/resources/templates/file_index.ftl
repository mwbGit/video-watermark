<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>文件助手</title>
</head>
<body>
<div style="text-align: center">
    <form action="/file/upload" method="post" enctype="multipart/form-data">
        <input type="file" name="file">
        <input type="submit" value="上传">
    </form>
    <span style="color: #666666" >最大上传50M文件，文件有效期30天</span>
    <br>
    <table border="1" style="margin: 0 auto; width: 800px;height: auto; table-layout: fixed">
        <thead>
        <tr>
            <td style="width: 10%">编号</td>
            <td >文件名</td>
            <td style="width: 25%">上传日期</td>
            <td style="width: 17%"></td>
        </tr>
        </thead>
        <tbody>
        <#list files as file>
            <tr>
                <td>${file.id?c}</td>
                <td style="word-wrap:break-word;" >${file.fileName}</td>
                <td>${file.addTimeDesc}</td>
                <td><a id="dowId" href="/file/download?fileId=${file.idDesc}" target="_self">下载</a>
                    <input type="button" onClick="CopyUrl()" value="复制链接" />
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</div>

<script type="text/javascript">
    function CopyUrl(){
        var host = "http://" + location.host;   //获取主机名+端口号          例如：172.20.11.111:8000
        var href = document.getElementById('dowId').getAttribute('href');
        var transfer = document.createElement('input');
        document.body.appendChild(transfer);
        transfer.value = host + href;
        transfer.focus();
        transfer.select();
        if (document.execCommand('copy')) {
            document.execCommand('copy');
        }
        transfer.blur();
        document.body.removeChild(transfer);
        alert("复制成功!");
    }
</script>
</body>
</html>