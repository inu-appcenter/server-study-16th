기존 방식의 문제점

하나의 조직에서 애플리케이션을 개발하고, 그 규모가 크다고 가정

하나의 애플리케이션을 개발하고, 테스트를 진행하며, 빌드를 한 후, 배포를 하는 일련의 과정 → 오랜 시간이 소요

또한, 이 애플리케이션은 한 사람이 관리하는 것이 아닌, 여러 사람으로 구성된 하나의 조직이 관리 

따라서 애플리케이션의 수정본을 하나로 합치는 것부터 배포까지 걸리는 시간까지 긴 배포주기를 갖게됨을 알 수 있음

각각의 수정본으로 진행하면 사용자의 피드백을 빠르게 반영할 수  없을 뿐더러, 배포 과정 속에서 문제가 발생할 가능성도 높음. 

⇒ 이러한 문제 상황을 개선하기 위해 등장한 단어가 CI/CD

**애플리케이션 코드 병합부터, 테스트, 배포까지의 라이플사이클 전체에 걸쳐 자동화를 이루어 보다 더 짧은 주기로 고객에게 애플리케이션을 제공하는 방법**

# CI /CD 란 무엇일까요?

https://github.com/inu-appcenter/server-study-16th/assets/109841880/a4c9127d-ff5b-47b3-92ec-cab32aacca6b

## 지속적 통합 (Continuous Integration, CI)

> 애플리케이션의 새로운 코드 변경사항이 정기적으로 빌드 및 테스트되어 공유레포지토리에 통합(merge)하는 것을 의미한다.
> 

CI는 개발자를 위한 자동화 프로세스라고 볼 수 있다.

이 프로세스의 단계로는 Code - Build - Test 단계로 나누어볼 수 있다. 

- Code : 개발자가 코드를 원격 코드 저장소에 push 하는 단계
- Build : 원격 코드 저장소로부터 코드를 가져와 유닛 테스트 후 빌드하는 단계
- Test : 코드 빌드의 결과물이 다른 컴포넌트와 잘 통합되는지 확인하는 과정

즉, `Build` & `Testing` 의 자동화이다. 

모든 코드 변화를 하나의 레포지토리에서 관리하는 것부터 시작한다. 모든 개발팀이 코드의 변화를 지속적으로 확인할 수 있기 때문에, 투명하게 문제점을 파악할 수 있다. 그리고 잦은 ***PR과 merge*** 로 코드를 자주 통합한다. 이때, 기본적인 테스트도 작동시킬 수 있다. 

이렇게 코드 변경사항을 지속적으로 통합하여 여러 명의 개발자가 코드 변경으로 인한 문제점을 빠르게 파악하고 수정하여 서로 충돌할 수 있는 문제를 해결할 수 있도록 한다. 

지속적 통합이 적용된 개발팀은 코드를 머지하기 전, 이미 빌드 오류나 테스트 오류를 확인하여 훨씬 더 효율적인 개발을 할 수 있게 된다. 

#### 지속적 통합 예시

github Actions workflows , Jenkins 

```java
name: CI/CD

on:
  push:
    branches: [ "master" ]
  pull_request:
    branches: [ "master" ]
    
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v1

      - name: Set Java
            uses: actions/setup-java@v3
        with:
          java-version: '11'
          distribution: 'temurin'

      - name: Setup Gradle
        uses: gradle/gradle-build-action@v2

      - name: Grant execute permission for gradlew
            run: chmod +x gradlew

      - name: Run build with Gradle Wrapper
        run: ./gradlew build

      - name: Send file to the server
        uses: appleboy/scp-action@master
        with:
            host: ${{ secrets.HOST }}
            username: ${{ secrets.USER }}
            password: ${{ secrets.PASSWORD }}
            port: ${{ secrets.SSH_PORT }}
            source: "./build/libs/*.jar"
            target: download

      - name: Deploy remote ssh commands using password
        uses: appleboy/ssh-action@master
        with:
          host: ${{ secrets.HOST }}
          username: ${{ secrets.USER }}
          password: ${{ secrets.PASSWORD }}
          port: ${{ secrets.SSH_PORT }}
          script_stop: true
          script: |
            sh deploy/deploy.sh
```

<img width="658" alt="buildCI" src="https://github.com/inu-appcenter/server-study-16th/assets/109841880/999e6c7e-b4b7-4782-a30c-e2e90564a555">

---

## **지속적 배포(Continuous Delivery/Deployment, CD)**

- 지속적인 서비스 제공 (Continuous Delivery)
    - 자동화한  CI과정을 거친 소스코드를 공유 레포지토리로 자동으로 Release 하는 것을 의미
    - 자동화하여 인간의 개입을 최소화

- 지속적인 배포 (Continuous Deployment)
    - Production 레벨까지 자동으로 deploy하는 것을 의미

즉, CD는 개발자의 변경사항이 레포지토리를 넘어, 고객의 프로덕션환경까지 릴리즈 되는 것을 의미. 

이 부분도 3가지 단계로 나누어 볼 수 있다.

- Release : 배포 가능한 소프트웨어 패키지를 작성
- Deploy : 프로비저닝을 실행하고 서비스를 사용자에게 노출 ⇒ 실질적인 배포
- Operate : 서비스 현황을 파악하고 생길 수 있는 문제를 감지

지속적 배포의 경우, 코드 변경 사항의 병합부터 프로덕션에 적합한 빌드 제공에 이르는 모든 단계로, 테스트 자동화와 코드 배포 자동화가 포함된다.

이 프로세스를 완료하면 프로덕션 준비가 완료된 빌드를 코드 레포지토리에 자동으로 배포할 수 있기 때문에 운영팀이 보다 빠르고 손쉽게 애플리케이션을 프로덕션으로 배포할 수 있게 된다. 

#### 배포 스크립트

```bash
#!/bin/bash
echo "> 현재 USER NAME을 가져옵니다."
CURRENT_USER=$(whoami)
echo "> download 폴더에서 build된 .jar 파일을 가져옵니다."
cp /home/$CURRENT_USER/download/build/libs/*.jar /home/$CURRENT_USER/deploy;
echo "> deploy 폴더에서 plain.jar 파일을 찾고, 파일이 있다면 삭제합니다."
find /home/$CURRENT_USER/deploy -name "*plain*" | xargs rm;
echo "> download 폴더를 삭제합니다."
rm -rf /home/$CURRENT_USER/download;

 echo "> 작동 중인 [JAR 이름]을 찾습니다."
 CURRENT_PID=$(pgrep -fo [JAR 이름])
 echo "$CURRENT_PID"
 if [ -z $CURRENT_PID ]; then
         echo "> 실행 중인 [JAR 이름]이 없습니다."
 else
         echo "> kill -9 $CURRENT_PID"
         kill -9 $CURRENT_PID
         sleep 3
 fi
 echo "> [JAR 이름]을 시작합니다."

nohup java -jar *.jar 1>stdout.txt 2>stderr.txt &

sleep 3
```

### 배포 자동화 → CI/CD 파이프라인

개발자가 배포할 때마다 일일히 빌드하고 배포하는 과정을 진행하는 것은 한두 번이면 충분하겠지만, 이러한 과정이 수없이 진행된다면 이 과정을 수행하는 것은 번잡스럽고 지루할 것이다. 그래서 이 수없이 진행되는 배포 과정을 자동화시키는 방법을 구축하게 되는데 이것을 CI/CD 파이프라인이라고 한다. 

https://github.com/inu-appcenter/server-study-16th/assets/109841880/2a0f23e6-8983-41fd-83c0-ce74d8fd871e

자동화되는 부분 : 코드가 빌드되면서 최종적으로 배포가 되는 단계까지 


# CI/CD 툴 - Jenkins / Circle CI  / GoCD

https://github.com/inu-appcenter/server-study-16th/assets/109841880/e9c86a92-95ab-4a4b-b665-67897f44e053

- Jenkins
    - Java 기반으로 다양한 플랫폼(MacOS, Windows, Linux)에서 이용 가능
    - 오픈소스, 무료
    - 확장성이 좋으며 클라우드 플랫폼과 통합되어 있음.
    - GUI 환경
    - 많은 플러그인을 지원, 쉽게 커스터마이징이 가능함.
    - 초기 설정에 시간이 소요됨.
    - 설정과 관리가 복잡함.
    
- GitHub Actions
    - 개인프로젝트를 위한 가장 인기있는 도구
    - 클라우드에서 동작
    - 코드 저장소와 직접 통합되어 사용자가 작성한 워크플로우 파일을 기반으로 자동화된  파이프라인 수행
    
- Circle CI
    - 다양한 플랫폼에서 사용
    - 설정하기 쉽고, GitHub등 버전 제어 시스템과 함께 사용 가능
    - 클라우드에서 호스팅되기 때문에 관리부담이 적음
    - CI/CD 파이프라인을 YAML을 이용하여 “워크플로우”를 제공함
    - 빠른 빌드와 테스트 속도 제공
    - 무료 플랙이 제한적, 높은 트래픽이나 큰 규모의 프로젝트에는 추가비용 발생
    
- GoCD
    - Throughtworks에서 제공하는 오픈소스 CI/CD 서버
    - 다른 도구와 차별화 요소는 VSM (Value Stream Map) 기능으로 파이프라인 전반에 걸친 완전한 종단 간 보기
    - YAML 및 JSON 형식으로 파이프라인을 구성함
    - Jenkins보다는 플러그인이 많지 않다

이외의 다양한 툴

- TeamCity
- Travis CI
- Bamboo
- Gitlab CI/CD
- CodeShip
- Codefresh
- Jenkins X
