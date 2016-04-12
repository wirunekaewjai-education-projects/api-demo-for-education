
ในขั้นตอนการทดสอบสามารถนำไปติดตั้งใน XAMPP หรือ MAMP ก็ได้
และอย่าลืมทำการแก้ไขการตั้งค่าฐานข้อมูลในแต่ละไฟล์ของ Demo ก่อนใช้งาน
  
สามารถใช้โปรแกรม POSTMAN ในการทดสอบรีเควสได้
  
---------  
---------
  
#### Demo 01 และ 02  
เป็นเนื้อหาเกี่ยวกับการเขียน Request & Response แบบ JSON โดยแต่ละตัวอย่างจะเพิ่มความละเอียดของโค้ดมากขึ้นเรื่อยๆ เพื่อให้ผู้ที่เริ่มต้นสามารถศึกษาได้อย่างเป็นลำดับ

#### Demo 03 ถึง 05
เป็นเนื้อหาที่เพิ่มระดับจาก 01, 02 โดยจะมีการเชื่อมต่อกับฐานข้อมูล MySQLi แบบง่ายๆ เพื่อให้เห็นภาพของ CRUD (Create, Read, Update และ Delete)
  
**- เอกสารประกอบ Demo 03 และ 04**  
https://goo.gl/TZMc1H

**- เอกสารประกอบ Demo 05**  
https://goo.gl/b526KX
  
---------
  
#### Demo 06  
ปรับโค้ดจาก 05 ในเรื่องของ Url ให้เรียกง่ายขึ้นโดยแบ่งงานตาม Request Method (POST, GET, PUT, DELETE)  
จาก 
- `POST` Demo05/01-Create.php
- `GET`  Demo05/02-Read.php
- `GET`  Demo05/03-ReadWithId.php
- `POST` Demo05/04-Update.php
- `POST` Demo05/05-Delete.php

เป็น
- `POST` Demo06/Posts.php
- `GET`  Demo06/Posts.php
- `GET`  Demo06/Posts.php?id={number}
- `PUT`  Demo06/Posts.php?id={number}
- `DELETE` Demo06/Posts.php?id={number}
  
**เอกสารประกอบ Demo 06**  
https://goo.gl/MQXQsO
  
---------  
---------
  
**Future Plan**  
Demo 07++ เพิ่มระบบบัญชีผู้ใช้และการอัพโหลดไฟล์
