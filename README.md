# commentSearchApplication
댓글 검색 어플리케이션   

## 1. 목표
+ 1단계 : 색인 및 검색(id/full-text) 구현.   
+ 2단계 : aggregation 을 이용한 다양한 결과 도출   

## 2. application 구성

![구성도](image/구성.PNG)

Docker와 ElasticSearch 를 사용했다.   
일반적으로 기업에서 리눅스를 사용해 서비스를 제공하기 때문에 환경을 맞춰주기 위해서 Docker를 사용했다.
elasticsearch는 RDBMS와 비교하여 트랜잭션 ACID(원자성, 일관성, 고립성, 지속성) 를 보장할수 없어서 데이터 유실이 생길 수 있다.   
그러나 data를 새로 입력하거나 수정할 일이 없었고 검색에는 좋은 성능을 유지할 수 있기 때문에 사용했다.   
또한 전문검색을 BIGRAM 으로 제공하기 위하여 elasticsearch 를 사용했다.
도커 container의 데이터 휘발성 때문에 색인한 파일이 사라지기 때문에 data의 영속성을 위해 volume을 사용했다.   

## 3. docker 실행 command
elasticsearch 설치   
`docker image pull elasticsearch:6.8.7`   

elasticsearch 실행   
`docker run -v /c/ToyProject/data:/usr/share/elasticsearch/data -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:6.8.7`

ping    
`curl XGET- "hostip:9200"`   
![ping 결과](image/ping.PNG)

## 4. 댓글 색인 필드 타입

![매핑타입](image/mapping.png)    
search_field는 comment_title 과 comment_content 를 같이 저장한 필드로   
NGram Tokenizer를 이용하여 n-gram 검색이 가능하게 했다.
* text : 설명 혹은 길이가 긴 텍스트가 있는 필드에서 전문 검색이 필요한 경우에 사용하며, 색인 전에 분석을 거쳐 검색에 활용된다.
* keyword : 문자열 필드 분석이 가능한 타입이며, 해당 타입은 정렬 / 필터링 / 집계 기능을 지원한다.

## 5. 실행 결과

다음은 id로 댓글 검색한 예시 요청/결과이다.   
![id_검색](image/id_검색.PNG)   

다음은 댓글의 제목, 본문 내용에 대한 검색 예시 요청/결과이다.   
![키워드_검색_2](image/키워드_검색.PNG)

## 6. 결론
수집/색인/검색 모두 구현해보면서 계획대로 결과는 잘 나온거 같음   
댓글 검색에 활용하려면 다양한 검색 기능을 추가적으로 개발할 필요성이 있음   
