<?php

// อ่านค่าไอดีและเก็บไว้ที่ $id
$id = $_GET['id'];

// ถ้า Connect ไม่ได้จะจบการทำงานทันที
$conn = database_connect();

// สร้าง SQL Command สำหรับลบ
$sql = "DELETE FROM `images` WHERE `id`=$id";

// ถ้าทำงานผิดพลาดจะจบการทำงานทันที
database_query_boolean($conn, $sql);

// ปิดการเชื่อมต่อ MySQL
$conn->close();

// 204 : OK (No Content)
http_response_code(204);

// จบการทำงานแบบไม่มีการแสดงผลใดๆ

?>
