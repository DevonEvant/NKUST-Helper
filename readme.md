# 注意！
本專案已經轉至「組織serval」下繼續開發，請移駕至(下述需加入開發團隊才能查看)：
 - [serval-task-flow/nkust-crawler （nkust 校務系統 API）](https://github.com/serval-task-flow/nkust_crawler)
 - [serval-task-flow/NKUST-Helper （nkust 校務系統 APP）](https://github.com/serval-task-flow/NKUST-Helper)

# nkust 校務系統 API 介紹 
 * [account](#account)
 * [bus](#bus)
 * [classInfo](#classinfo)
 * [client](#client)
 * [請假](#)
 * [officialInfo](#officialinfo)
 * [studentInfo](#studentinfo)
 * [websiteLibrary](#websitelibrary)

（2023/1/18更新）

## account
學生帳號
- 管理登入(✓)
- 登出(✓)
- 儲存帳號密碼及與帳號有關的cookies(✗手機端完成)

## bus
校內公車 - _**ignore**_
- 查詢會員狀態(✓) - queryMemberState()
- 查詢可預約車輛(✓) - queryWithDate()
- 預約搭車(✓) - reserve()
- 取消預約搭車(✓) - cancelReserve()
- 查詢預約紀錄(✓) - queryReserved()
- 查詢搭車記錄(✗)
- 查詢罰鍰(✗)

## classInfo

- 取得所有學期(✓) - getCurriculumDropDown()
- 課程資訊(✓) - getCourse()
- 成績(✓) - getGrade()
- 成績預警(✓) - earlyGradeWarning()

## client

處裡https請求的地方，

## 請假
- 申請假單()
- 查詢請假()
- 缺曠(✓) - getAbsenteeismRecord()

## officialInfo
- 校務資訊()
- 校內地圖()
- 各處所資訊()
- 電話()
- 行事曆()

## studentInfo
學生個資
- 姓名(✓) - getNameCn() getNameEn()
- 學號(✓) - getID()
- 班級(✓) - getClassGrouping()
- 系所(✓) - getDepartment()
- 教室通行碼(✓) - getClassroomPass()
- 圖書館通行碼(✓) - getLibraryPassQrcode()
- 在學證明(✓) - getCertificateOfEnrollment()

## websiteLibrary
用於需要為校內不同網站個別實作程式的地方，包含處裡網站跳轉問題、網站登入

