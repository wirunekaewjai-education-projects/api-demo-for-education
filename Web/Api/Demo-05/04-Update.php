<?php

// อ่านเรื่อง require & include ได้ที่ http://www.w3schools.com/php/php_includes.asp
// หรือ http://www.thaiseoboard.com/index.php?topic=70259.0;wap2

// แทรกโค้ดจาก _Helper.dp
require_once('_Helper.php');

// ถ้าไม่ใช่ POST จะจบการทำงานทันที
filter_request('POST');

// ถ้า id เป็นค่าว่างหรือไม่ได้ส่งมาจะจบการทำงานทันที
filter_empty_vars($_POST, ['id']);

// ถ้า id ไม่ใช่ตัวเลขให้จบการทำงานทันที
filter_numeric_vars($_POST, ['id']);

// อ่านค่าไอดีและเก็บไว้ที่ $id
$id = $_POST['id'];

// สร้าง Array ของชื่อฟิลด์ที่จะอัพเดท
$fields = parse_fields_to_array($_POST, 'fields', []);

$set_array = [];

// อ่านเรื่อง in_array ได้ที่ http://php.net/manual/en/function.in-array.php
if(in_array('title', $fields))
{
    // ถ้า title เป็นค่าว่างหรือไม่ได้ส่งมาจะจบการทำงานทันที
    filter_empty_vars($_POST, ['title']);

    // อ่านเรื่อง trim ได้ที่ : http://www.w3schools.com/php/func_string_trim.asp
    // trim เพื่อลบ white space ซ้ายขวาของสตริง เช่น "  hello world!  " เป็น "hello world!";
    $text = trim($_POST['title']);
    $set_array[] = "`title`='$text'";
}

if(in_array('excerpt', $fields))
{
    // ถ้า excerpt เป็นค่าว่างหรือไม่ได้ส่งมาจะจบการทำงานทันที
    filter_empty_vars($_POST, ['excerpt']);

    $excerpt_length = 64;
    $text = substr(trim($_POST['excerpt']), 0, $excerpt_length);
    $set_array[] = "`excerpt`='$text'";
}

if(in_array('content', $fields))
{
    // ถ้า content เป็นค่าว่างหรือไม่ได้ส่งมาจะจบการทำงานทันที
    filter_empty_vars($_POST, ['content']);

    $text = trim($_POST['content']);
    $set_array[] = "`content`='$text'";
}

// อ่านเรื่อง implode ได้ที่ http://php.net/manual/en/function.implode.php
// ต่อสตริงจากสตริงอาร์เรย์ด้วย ', '
$set_sql = implode(', ', $set_array);

if(empty($set_sql) === true)
{
    // 400 : Bad Request
    http_response_code(400);

    die();
}

// ถ้า Connect ไม่ได้จะจบการทำงานทันที
$conn = database_connect();

// สร้าง SQL Command สำหรับอัพเดท
$sql = "UPDATE `posts`
        SET $set_sql, `modified_date`=NOW()
        WHERE `id`=$id";

// ถ้าทำงานผิดพลาดจะจบการทำงานทันที
database_query_boolean($conn, $sql);

// ปิดการเชื่อมต่อ MySQL
$conn->close();

// 200 : OK
http_response_code(200);

// จบการทำงานแบบไม่มีการแสดงผลใดๆ

?>
