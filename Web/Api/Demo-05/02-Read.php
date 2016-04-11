<?php

// อ่านเรื่อง require & include ได้ที่ http://www.w3schools.com/php/php_includes.asp
// หรือ http://www.thaiseoboard.com/index.php?topic=70259.0;wap2

// แทรกโค้ดจาก _Helper.dp
require_once('_Helper.php');

// ถ้าไม่ใช่ GET จะจบการทำงานทันที
filter_request('GET');

// กำหนดค่า Default ให้แต่ละพารามิเตอร์ที่จะใช้ Query
// unsigned int ตั้งแต่ 0 - 1000
$limit = parse_int($_GET, 'limit', 20, 0, 1000);

// unsigned int ตั้งแต่ 0 ขึ้นไป
$offset = parse_int($_GET, 'offset', 0, 0);

// อ่านเรื่อง Offset และ Limit ได้ที่ http://www.w3schools.com/php/php_mysql_select_limit.asp
/*
    ตัวอย่างการใช้ Offset และ Limit เพื่อทำ Pagination
    - แบบที่ 1
    Page 1 : http://abc.com/api/posts?limit=10
    Page 2 : http://abc.com/api/posts?offset=10&limit=10
    Page 3 : http://abc.com/api/posts?offset=20&limit=10
    Page 4 : http://abc.com/api/posts?offset=30&limit=10

    - แบบที่ 2 :: ใช้ Default limit คือ 20
    Page 1 : http://abc.com/api/posts
    Page 2 : http://abc.com/api/posts?offset=20
    Page 3 : http://abc.com/api/posts?offset=40
    Page 4 : http://abc.com/api/posts?offset=60
*/

// string 'DESC' | 'ASC'
$order = parse_string($_GET, 'order', 'DESC', [ 'DESC', 'ASC' ]);

// string 'id' | 'title' | 'created_date' | 'modified_date'
$order_by_availables = [ 'id', 'title', 'created_date', 'modified_date' ];
$order_by = parse_string($_GET, 'order_by', 'created_date', $order_by_availables);

// สร้าง Array ของชื่อฟิลด์ที่อนุญาตให้ใช้ได้
// string 'id' | 'title' | 'excerpt' | 'content' | 'created_date' | 'modified_date'
$field_default = "`id`, `title`, `excerpt`, `created_date`";
$fields = parse_fields($_GET, 'fields', $field_default);

// ถ้า Connect ไม่ได้จะจบการทำงานทันที
$conn = database_connect();

// อ่านเรื่อง SQL (MySQL) ได้ที่ : http://www.w3schools.com/php/php_mysql_intro.asp
// สร้าง SQL Command สำหรับการอ่านข้อมูล
$sql = "SELECT $fields
        FROM `posts`
        ORDER BY `$order_by` $order
        LIMIT $limit
        OFFSET $offset";

// รันคำสั่ง SQL
$result = $conn->query($sql);

if($result === false)
{
    // ปิดการเชื่อมต่อ MySQL
    $conn->close();

    // 400 : Bad Request
    http_response_code(400);
    die();
}

// สร้างตัวแปรอาร์เรย์เพื่อเก็บผลลัพธ์
$array = [];

// ถ้า Query แล้วมีข้อมูลจะต้องได้ num_rows > 0
if($result->num_rows > 0)
{
    // Query แบบ assoc (Associative Array) : http://www.w3schools.com/php/php_mysql_select.asp
    // อ่านเรื่อง Associative Array ได้ที่ : http://www.w3schools.com/php/php_arrays.asp

    // While-Loop : http://www.w3schools.com/php/php_looping.asp
    while($row = $result->fetch_assoc())
    {
        // สร้าง Associative Array เพื่อเก็บข้อมูลในแต่ละ row
        $item = [];

        foreach ($row as $key => $value)
        {
            if($key === 'id')
            {
                // อ่านเรื่อง intval ได้ที่ : http://php.net/manual/en/function.intval.php
                $item[$key] = intval($value);
            }
            else
            {
                $item[$key] = $value;
            }
        }

        // อ่านเรื่องการเพิ่มข้อมูลได้ที่ : http://php.net/manual/ru/function.array-push.php
        // เพิ่มข้อมูลลงในอาร์เรย์
        $array[] = $item;
    }
}

// ปิดการเชื่อมต่อ MySQL
$conn->close();

response_json($array);

?>
