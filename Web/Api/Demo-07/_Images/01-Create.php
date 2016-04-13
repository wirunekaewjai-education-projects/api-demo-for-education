<?php

if(empty($_FILES['image']) === true)
{
    // 400 : Bad Request
    http_response_code(400);
    die();
}

// อัพโหลดรูป
$current_file = basename($_FILES['image']["name"]);
$ext = pathinfo($current_file, PATHINFO_EXTENSION);

if($ext !== "jpg" && $ext !== "png" && $ext !== "jpeg" && $ext !== "gif" )
{
    // 406 : Not Acceptable
    http_response_code(406);
    die();
}

$target_dir = "Uploads/";
$target_name = uniqid() . '.' . $ext;
$target_file = $target_dir . $target_name;

if (file_exists($target_dir) === false)
{
    mkdir($target_dir, 0755, true);
}

if (move_uploaded_file($_FILES['image']["tmp_name"], $target_file) === false)
{
    // 500 : Internal Server Error
    http_response_code(500);
    die();
}

// สร้าง SQL Command สำหรับเพิ่มโพส
$sql = "INSERT INTO `images`(`name`)
        VALUES ('$target_name')";

// ถ้า Connect ไม่ได้จะจบการทำงานทันที
$conn = database_connect();

// ถ้าทำงานผิดพลาดจะจบการทำงานทันที
database_query_boolean($conn, $sql);

// ------- เพิ่มข้อมูลสำเร็จ ลำดับต่อไปคือแสดงผล -------- //
// อ่านเรือง Last Insert ID ได้ที่ http://www.w3schools.com/php/php_mysql_insert_lastid.asp
// อ่านค่าไอดีที่เพิ่มลงในฐานข้อมูลล่าสุด
$inserted_id = $conn->insert_id;

// ปิดการเชื่อมต่อ MySQL
$conn->close();

$object =
[
    'id' => $inserted_id,
    'name' => $target_name
];

// 201 : OK - Created
response_json($object, 201);

?>
