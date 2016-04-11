<?php

// อ่านเรื่อง require & include ได้ที่ http://www.w3schools.com/php/php_includes.asp
// หรือ http://www.thaiseoboard.com/index.php?topic=70259.0;wap2

// แทรกโค้ดจาก _Helper.dp
require_once('_Helper.php');

// ถ้าไม่ใช่ POST จะจบการทำงานทันที
filter_request('POST');

// ถ้า id, title เป็นค่าว่างหรือไม่ได้ส่งมาจะจบการทำงานทันที
filter_empty_vars($_POST, ['id']);

// ถ้า id ไม่ใช่ตัวเลขให้จบการทำงานทันที
filter_numeric_vars($_POST, ['id']);

// อ่านค่าไอดีและเก็บไว้ที่ $id
$id = $_POST['id'];

// ถ้า Connect ไม่ได้จะจบการทำงานทันที
$conn = database_connect();

// สร้าง SQL Command สำหรับลบ
$sql = "DELETE FROM `posts` WHERE `id`=$id";

// ถ้าทำงานผิดพลาดจะจบการทำงานทันที
database_query_boolean($conn, $sql);

// ปิดการเชื่อมต่อ MySQL
$conn->close();

// 200 : OK
http_response_code(200);

// จบการทำงานแบบไม่มีการแสดงผลใดๆ

?>
