# `template-engine`
> Collection을 주어진 템플릿에 맞게 변환하여 출력하는 템플릿 엔진입니다.       

## Description

- _**Template 구조와 Entity 구조만 일치하면 모든 Json데이터 처리 가능**_
- `Template` 구현을 통한 기능 확장 가능
- `Resolver` 구현을 통해 파일, 웹 리소스등 `Reader` 선택 가능 
    - `FileResolver`, `URIResolver` 구현
- `TemplateParser`를 통해 Top 템플릿, Element 별 템플릿 동일 파싱 로직으로 일괄처리
    - 내부 템플릿 반복 변환 재귀호출 구조
    - 특정필드가 명시된 로직 없음! 템플릿 명시 필드명 기준 추출 ex)book 샘플
    - 템플릿에 명시된 키가 최종 파싱되는 키값이 아닐 경우는 하위 JSON 문자열로 그대로 출력
- `Writer` 전달 받아 원하는 양식으로 출력 가능  (File, System 등)
- `TemplateException` 을 통한 예외처리

## Structure
```bash
├── template              # default template folder
│   └──[template files]
├── data                  # default data folder
│   └──[data files]
├── lib                   # lib
│   └── json-simple-1.1.1.jar
├── src                   # source
│   ├── Main.java
│   ├── constant          
│   ├── core
│   ├── exception
│   ├── resolver
│   └── util
│
├── README.md             # README.md
├── template.sh           # 실행 ShellScript 
├── template-engine.jar   # 실행 jar 
├── template.properties   # 설정 파일 - Template,data,경로,파일명 등 
└── output.txt            # Default Result file
```
### template.properties
- Template, Data 적용 우선순위
  - 1순위 args[]
  - 2순위 template.properties
```properties
# Base Location
prefix.template=template/
prefix.data=data/
prefix.result=

# Resource info
template=sample_hard
data=user.json
data.uri = https://gist.githubusercontent.com/IfUwanna/49944ed3a3de81e719f2db14660ff1e1/raw/91682033cebfd89a189e388322cef4cfb9abf2bf/user.json
result=output.txt

### template sample
# sample(하,중,상) : sample_easy / sample_medium / sample_hard
# custom-user : user / user_top
# custom-book : book / book_top [!!change data ::: book.json]

### data sample
# user.json (sample)
# user_custom.json
# book.json
```

## Usage
```bash
#java 11+
usage: ./template.sh [tempalte] [data] 

./template.sh 
./template.sh sample_easy user.json      # 난이도 하 
./template.sh sample_medium user.json    # 난이도 중
./template.sh sample_hard user.json      # 난이도 상 
./template.sh user user.json             # user (custom template)
./template.sh user_top user.json         # user (custom template)
./template.sh book book.json             # book (custom template)
./template.sh book_top book.json         # book (custom template)
```

## Example Samples

> 확인하시기 쉽게 파일 출력(output.txt)과 더불어 PrintWriter System 출력도 추가해 뒀습니다.
### 1. sample_easy / user.json  - 난이도 하
```text
➜  template-engine (master) ✗ ./template.sh sample_easy user.json
================ Jihun template-engine start :) ================
Family name: KIM
Given name: HS
Address : ABC CDE
MemberShip : GOLD 12345

Family name: Doe
Given name: John
Address : AAA BBB
MemberShip : SILVER 67890

Family name: Doe
Given name: Jane
Address :  
MemberShip : BRONZE 99999

```
### 2. sample_medium / user.json  - 난이도 중
```text
➜  template-engine (master) ✗ ./template.sh sample_medium user.json
================ Jihun template-engine start :) ================
Name: HS KIM
Address : ABC CDE

Name: John Doe
Address : AAA BBB
Address : 123 234

Name: Jane Doe

```
### 3. sample_hard / user.json  - 난이도 상
```text
➜  template-engine (master) ✗ ./template.sh sample_hard user.json
================ Jihun template-engine start :) ================
Admin
Membership Id : 12345
Membership Id : 67890
Membership Id : 99999

```
### 4. user / user.json  - custom template
```text
[User]\n
family name: <?=USER.info.name.family?>\n
Given name: <?=USER.info.name.given ?>\n
middle name: <?=USER.info.name.middle ?>\n
<? for ADDR in USER.info.addrs ?>
Address : <?= ADDR.addr1?> / <?= ADDR.addr2?>\n
<? endfor ?>
MemberShip : <?=USER.membership.grade?> / <?= USER.membership.id ?>\n
\n
```
```text
➜  template-engine (master) ✗ ./template.sh user user.json
================ Jihun template-engine start :) ================
[User]
family name: KIM
Given name: HS
middle name: 
Address : ABC / CDE
MemberShip : GOLD / 12345

[User]
family name: Doe
Given name: John
middle name: Siva
Address : AAA / BBB
Address : 123 / 234
MemberShip : SILVER / 67890

[User]
family name: Doe
Given name: Jane
middle name: 
MemberShip : BRONZE / 99999

```
### 5. user_top / user.json  - custom template
```text
[Users name]\n
<? for family in USERS.*.info.name ?>
Full Name Json : <?= Name ?>\n
<? endfor ?>
<? for family in USERS.*.info.name.family ?>
Family Name : <?= FAMILY ?>\n
<? endfor ?>
<? for GIVEN in USERS.*.info.name.given ?>
Given Name : <?= GIVEN ?>\n
<? endfor ?>
<? for middle in USERS.*.info.name.middle ?>
Middle Name : <?= middle ?>\n
<? endfor ?>
\n
[Users Address]\n
<? for ADDRS in USERS.*.info.addrs ?>
Address : <?= ADDRS.addr1?> / <?= ADDRS.addr2?>\n
<? endfor ?>
\n
[Users Membership]\n
<? for ID in USERS.*.membership.id ?>
Membership Id : <?= ID ?>\n
<? endfor ?>
<? for GRADE in USERS.*.membership.grade ?>
Membership GRADE : <?= GRADE ?>\n
<? endfor ?>
\n
```
```text
➜  template-engine (master) ✗ ./template.sh user_top user.json
================ Jihun template-engine start :) ================
[Users name]
Full Name Json : {"given":"HS","family":"KIM"}
Full Name Json : {"given":"John","middle":"Siva","family":"Doe"}
Full Name Json : {"given":"Jane","family":"Doe"}
Family Name : KIM
Family Name : Doe
Family Name : Doe
Given Name : HS
Given Name : John
Given Name : Jane
Middle Name : 
Middle Name : Siva
Middle Name : 

[Users Address]
Address : ABC / CDE
Address : AAA / BBB
Address : 123 / 234

[Users Membership]
Membership Id : 12345
Membership Id : 67890
Membership Id : 99999
Membership GRADE : GOLD
Membership GRADE : SILVER
Membership GRADE : BRONZE

```
### 6. book / book.json - custom template(book)
```text
[Book]\n
name: <?=BOOK.info.name?>\n
<? for CONTENT in BOOK.info.contents ?>
contents : <?= CONTENT.title?> / <?= CONTENT.description?>\n
<? endfor ?>
quantity : <?=BOOK.quantity.total ?> / <?=BOOK.quantity.ordered ?> / <?=BOOK.quantity.remain ?>\n
price: <?=BOOK.price.normal ?> >> <?=BOOK.price.sale ?> sale!\n
\n
```
```text
➜  template-engine (master) ✗ ./template.sh book book.json
================ Jihun template-engine start :) ================
[Book]
name: 수학책
contents : 목차1 / 111
contents : 목차2 / 222
contents : 목차3 / 333
quantity : 100 / 3 / 97
price: 10000 >> 9000 sale!

[Book]
name: 도덕책
contents : 목차1 / 111
contents : 목차2 / 222
quantity : 999 / 7 / 992
price: 30000 >> 9000 sale!

```
### 7. book_top / book.json - custom template(book)
```text
[Book Name]\n
<? for NAME in BOOKS.*.info.name ?>
NAME : <?= NAME ?>\n
<? endfor ?>
\n
[Book Contents]\n
<? for CONTENTS in BOOKS.*.info.contents ?>
contents : <?= CONTENTS.title?> <?= CONTENTS.description?>\n
<? endfor ?>
\n
[Book Quantity]\n
<? for total in BOOKS.*.quantity.total ?>
total : <?= total?>\n
<? endfor ?>
\n
[Book Price]\n
<? for PRICE in BOOKS.*.price.normal ?>
price : <?= PRICE?>\n
<? endfor ?>
\n
```
```text
➜  template-engine (master) ✗ ./template.sh book_top book.json
================ Jihun template-engine start :) ================
[Book Name]
NAME : 수학책
NAME : 도덕책

[Book Contents]
contents : 목차1 111
contents : 목차2 222
contents : 목차3 333
contents : 목차1 111
contents : 목차2 222

[Book Quantity]
total : 100
total : 999

[Book Price]
price : 10000
price : 30000

```
