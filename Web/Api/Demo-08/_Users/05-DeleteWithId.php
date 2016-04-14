<?php

// Login ก่อนทำงาน
filter_basic_auth();

// อ่านค่าไอดีและเก็บไว้ที่ $id
$id = $_GET['id'];

// ถ้า Connect ไม่ได้จะจบการทำงานทันที
$conn = database_connect();

// สร้าง SQL Command สำหรับลบ
$sql = "DELETE FROM `users` WHERE `id`=$id";

// ถ้าทำงานผิดพลาดจะจบการทำงานทันที
database_query_boolean($conn, $sql);

// ตรวจสอบว่าอัพเดทได้จริงหรือไม่ ?
// ถ้าได้จะต้องมีค่ามากกว่า 0 (ในที่นี้ควรจะไม่เกิน 1 เพราะเราลบแค่ id เดียว)
if($conn->affected_rows > 0)
{
    // 200 : OK (No Content)
    http_response_code(204);
}
else
{
    // 404 : Not Found
    http_response_code(404);
}

// ปิดการเชื่อมต่อ MySQL
$conn->close();

?>
