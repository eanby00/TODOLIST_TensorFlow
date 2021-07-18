
# TODO list with TensorFlow Lite

  

## 프로젝트 개요

- 안드로이드 기반의 TODO list 개발
- 사용자의 일정의 난이도를 측정하고 상, 하로 판단 
- 사용자가 달성한 일정의 비율을 측정하고 성취도를 상, 하로 판단
- 모델의 성능 향상을 위해 주기적으로 모델 업데이트

## 프로젝트 적용 기술

- Android, Android Studio
- python, TensorFlow
- TensorFlow Lite
- DB(Room)

## 프로젝트 주안점

- Android 앱 디자인
- TensorFlow를 통해 일정의 난이도 분류 모델, 달성한 일정의 비율 분류 모델 생성
- TensorFlow Lite를 통해 Android 앱에 모델 적용
- 프로젝트에 데이터베이스 적용

## 프로젝트 구성

- 메인 페이지
	- 달력
	- 각 날짜별로 모종의 수단을 이용해 성취도가 높은지 낮은지 확인 가능
- 세부 페이지
	- 각 날짜별 해야 할 TODO LIST
	- 생성한 TODO LIST의 난이도를 상, 하로 분류
		- 평균을 기준으로 각 날짜의 난이도를 상, 하로 분류
		- 이것을 이용해 모델 생성
	-	실제 달성한 TODO LIST의 비율로 성취도를 상, 하 판단
- reference: 참고한 사이트들 정리

## 프로젝트 배운 점

  

## 프로젝트 간의 연관성

  

## 프로젝트 키워드

  

## 프로젝트 링크

  

## 프로젝트 reference

- TensorFlow 공식문서: https://www.tensorflow.org/?hl=ko
- TensorFlow Lite 공식문서: https://www.tensorflow.org/lite?hl=ko
- 커스텀 달력: https://github.com/ahmed-alamir/CalendarView
