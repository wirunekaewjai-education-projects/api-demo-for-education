<?php

// อ่านเรื่อง require & include ได้ที่ http://www.w3schools.com/php/php_includes.asp
// หรือ http://www.thaiseoboard.com/index.php?topic=70259.0;wap2

// แทรกโค้ดจาก _Helper.dp
require_once('_Helper.php');

// ถ้าไม่ใช่ POST จะจบการทำงานทันที
filter_request('POST');

// ถ้า id, title เป็นค่าว่างหรือไม่ได้ส่งมาจะจบการทำงานทันที
filter_empty_vars($_POST, ['id', 'title']);

// ถ้า id ไม่ใช่ตัวเลขให้จบการทำงานทันที
filter_numeric_vars($_POST, ['id']);

// อ่านค่าไอดีและเก็บไว้ที่ $id
$id = $_POST['id'];

// อ่านเรื่อง trim ได้ที่ : http://www.w3schools.com/php/func_string_trim.asp
// trim เพื่อลบ white space ซ้ายขวาของสตริง เช่น "  hello world!  " เป็น "hello world!";
$title = trim($_POST['title']);

// อ่านเรื่อง Ternary Operator ()?: ได้ที่ : http://www.sitepoint.com/using-the-ternary-operator/
// ถ้า content ไม่ถูกส่งมาให้กำหนดเป็นสตริงว่าง
$content = empty($_POST['content']) ? '' : trim($_POST['content']);

// อ่านเรื่อง substr ได้ที่ http://www.w3schools.com/php/func_string_substr.asp
// ทำการย่อ content ให้เหลือแค่สั้นๆ ไม่เกิน 64 ตัวอักษร
$excerpt_length = 64;
$excerpt = trim(substr($content, 0, $excerpt_length));

// ถ้า Connect ไม่ได้จะจบการทำงานทันที
$conn = database_connect();

// สร้าง SQL Command สำหรับอัพเดท
$sql = "UPDATE `posts`
        SET `title`='$title', `excerpt`='$excerpt', `content`='$content', `modified_date`=NOW()
        WHERE `id`=$id";

// ถ้าทำงานผิดพลาดจะจบการทำงานทันที
database_query_boolean($conn, $sql);

// ปิดการเชื่อมต่อ MySQL
$conn->close();

// 200 : OK
http_response_code(200);

// จบการทำงานแบบไม่มีการแสดงผลใดๆ

?>
