<?php

// อ่านเรื่อง HTTP POST ได้ที่ http://www.w3schools.com/tags/ref_httpmethods.asp
// อ่านเรื่องตัวแปร $_xxx ได้ที่ : http://www.w3schools.com/php/php_superglobals.asp
// อ่านเรื่องเครื่องหมาย !== ได้ที่ : http://www.w3schools.com/php/php_operators.asp
if($_SERVER['REQUEST_METHOD'] !== 'POST')
{
    // คำอธิบาย HTTP Response Code : https://goo.gl/MXVo6k
    // คำอธิบายฟังก์ชัน http_response_code : http://php.net/manual/en/function.http-response-code.php

    // 405 : Method Not Allowed
    http_response_code(405);

    // อ่านเรื่องฟังก์ชัน die() ได้ที่ : http://www.w3schools.com/php/func_misc_die.asp
    // จบการทำงานของไฟล์ทันทีเพราะรูปแบบรีเควสไม่ใช่ POST
    die();
}

// อ่านเรื่อง empty ได้ที่ : http://goo.gl/IDKN1i, https://goo.gl/pntS6D
// และ http://php.net/manual/en/function.empty.php
if(empty($_POST['id']) === true)
{
    // 400 : Bad Request
    http_response_code(400);
    die();
}

// อ่านเรื่อง is_numeric ได้ที่ http://www.w3resource.com/php/function-reference/is_numeric.php
if(is_numeric($_POST['id']) === false)
{
    http_response_code(400);
    die();
}

// อ่านค่าไอดีและเก็บไว้ที่ $id
$id = $_POST['id'];

if(empty($_POST['title']) === true)
{
    // 400 : Bad Request
    http_response_code(400);
    die();
}

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

// ถ้าต้องการนำไปใช้กับเซิฟเวอร์ตัวเองให้แก้ข้อมูลให้ตรงด้วย
$server     = 'localhost';
$database   = 'simple_post_db';
$username   = 'root';
$password   = '';

// เชื่อมต่อฐานข้อมูล MySQL
// อ่านเพิ่มเติมได้ที่ : http://www.w3schools.com/php/php_mysql_connect.asp
$conn = new mysqli($server, $username, $password, $database);

// ตรวจสอบการเชื่อมต่อ
if ($conn->connect_error)
{
    // 500 : Internal Server Error
    http_response_code(500);
    die();
}

// สร้าง SQL Command สำหรับอัพเดท
$sql = "UPDATE `posts`
        SET `title`='$title', `excerpt`='$excerpt', `content`='$content', `modified_date`=NOW()
        WHERE `id`=$id";

// รันคำสั่ง SQL
$result = $conn->query($sql);

// ปิดการเชื่อมต่อ MySQL
$conn->close();

if($result !== TRUE)
{
    // 400 : Bad Request
    http_response_code(400);
}
else
    // 200 : OK
    http_response_code(200);
}

?>
