

Data/JSON 에 API키를 넣어야 연결 가능

  각 api키 파일 이름
  Dialogflow : APIkey_Dialogflow.json
  GCPVision  : APIkey_GCPVision.json


폴더
Connecter : 각각의 api 연결
Data : api 키와 테스트용 이미지 저장중
Routes : 웹 서버 라우팅



파일
.jshintrc : atom (linter-jshint) 설정파일  - 내가 쓰고있는 편집기 설정파일
package.json : 프로젝트 정보들, 모듈도 여기서 참고
test.js : express.js를 사용하지 않은 테스트용
index.js : 실행할 파일
WebServer.js : 메인 코드
