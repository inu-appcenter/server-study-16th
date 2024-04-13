# 1주차_서버는 무엇이고 어떻게 동작할까? / 자바 웹 프레임워크는 어떻게 변화해 왔을까?

생성일: 2024년 3월 29일 오후 3:04
태그: 서버 스터디

### 서버는 무엇이고 어떻게 동작할까요?, 자바 웹 프레임워크는 어떻게 변화해 왔을까요?

- 서버와 클라이언트는 무엇인가요?
- 자바 웹 프레임워크의 역사
    - J2EE(JavaEE), EJB, Servlet, JSP는 무엇일까요?
    - 스프링 프레임워크는 어떻게 탄생하게 되었나요?

# 서버란?

> ***ser·ver [**명사]*
> 
> 1. *(컴퓨터의) 서버*
> 2. *(테니스 등에서) 서브하는 사람*
> 3. *美 (식당에서) 서빙하는 사람, 웨이터, 웨이터리스*

서버는 ‘제공하다’의 뜻을 가진 ‘serve’에 사람이라는 뜻을 나타내는 ‘er’을 붙인 단어이다. 즉, 무언가를 제공하는 이를 서버라고 부른다는 것을 알 수 있다. 테니스, 배구 같은 스포츠에서는 처음 공을 던지는(제공하는) 사람을 서버라고 부르고,  식당에서는 손님에게 음식 등을 전달하는(제공하는) 사람을 서버라고 부른다. 그렇다면 컴퓨터에서 서버는 무엇일까?

![001-1-1](https://github.com/inu-appcenter/server-study-16th/assets/86196038/5fed29be-879a-4c69-a761-0a65cf05f36f)

> ***서버**(server)는 클라이언트에게 네트워크를 통해 정보나 서비스를 제공하는 컴퓨터 시스템*
> 

서버에는 여러가지가 있다.

- 웹 서버: 웹 사이트 서비스를 제공하기 위한 서버
- 도메인 서버 : 도메인을 관리하기 위한 서버
- 이미지 서버 : 이미지를 관리하기 위한 서버
- 이메일 서버 : 이메일을 관리하기 위한 서버
- DB 서버 : 데이터 정보를 관리하기 위한 서버
- 게임 서버 : 게임을 제공하기 위한 서버

또한 서버는 컴퓨터나 장치 같은 ‘하드웨어’만을 의미하지 않는다. 기능을 제공하도록 도와주는 ‘소프트웨어’도 서버라는 개념에 포함된다.

이해를 돕기 위해 예를 들자면 홈페이지가 인터넷에 구현되기 위해서는 여러 종류의 서버가 필요하다. 그 중 가장 중요한 한 가지가 웹 서버 (web server)라고 할 수 있다.

![005-1](https://github.com/inu-appcenter/server-study-16th/assets/86196038/950b744a-ffc4-4986-aeb5-03d9b5e8d321)


웹 서버

홈페이지 제작이 되기 위한 서버를 하드웨어적 측면과 소프트웨어적 측면으로 나눠서 살펴보자면,

우선, 하드웨어적 측면에서 ‘웹 서버 컴퓨터’가 필요하다. 이는 각각의 목적에 맞춰서 한 대일 수도 여러 대가 될 수도 있다.

다음으로, 소프트웨어적인 측면에서 웹 서버 컴퓨터 (하드웨어)에 들어있는 홈페이지 재료들을 웹에 뿌려주고 동작하게 하는 ‘웹 서버 프로그램’이 필요하다. 웹 서버 프로그램도 아파치 (Apache), 엔진엑스 (Nginx) 등 여러 가지가 존재한다.

서버라는 개념은 하드웨어와 소프트웨어를 포함해 굉장히 많은 의미를 내포하고 있다. 따라서 서버는 누군가에게 서비스를 제공하는 ‘역할’이라고 보는 것이 좋다. 즉, 웹 서버는 클라이언트에게 웹 서비스를 제공하는 역할을 하는 것을 의미한다고 볼 수 있다.

### 클라이언트 서버 모델

서버는 단독으로 움직이지 않으며 불특정 다수의 컴퓨터에 일방적으로 서비스를 제공하는 것도 아니다. 서버는 **클라이언트로부터 요청(Request)를 받아야 비로소 처리를 시작하여 서비스를 제공**한다. 

서버가 클라이언트에게 서비스를 제공할 때 다음과 같은 처리가 일어난다.

1. 클라이언트가 서버에게 어떤 서비스를 요청
2. 서버는 요청에 응답해 처리를 수행
3. 서버는 처리 결과를 클라이언트에게 반환
4. 클라이언트는 처리 결과를 받음
    
    ![img1 daumcdn](https://github.com/inu-appcenter/server-study-16th/assets/86196038/2849bf10-e282-423e-8a2c-f86a3df0a43d)

    

이처럼 서버와 클라이언트로 구성된 시스템을 ‘클라이언트 서버 모델’이라고 한다. 

클라이언트 서버 모델은 서버에서 데이터를 쉽게 관리할 수 있기 때문에 대부분의 컴퓨터 시스템에서 채택하고 있고 웹 시스템은 클라이언트 서버 모델을 사용하는 대표적인 예이다.

### 웹 시스템에서 클라이언트 서버 모델

웹 시스템에서 클라이언트 서버 모델을 알아보기에 앞서 웹에서 클라이언트와 서버는 정확히 무엇을 말하는 것인지 알아보겠다.

웹 개발에서 **클라이언트 측**은 웹 애플리케이션에서 최종 사용자 장치(클라이언트)에 표시되거나 발생하는 모든 것을 의미한다. 여기에는 텍스트, 이미지, 나머지 UI 등 사용자에게 표시되는 내용과 애플리케이션이 사용자의 브라우저 내에서 수행하는 모든 작업이 포함된다. 

HTML, CSS 등의 마크업 언어는 클라이언트 측의 브라우저에서 해석된다. 최근에는 많은 개발자들이 애플리케이션 아키텍처에 클라이언트 측 프로세스를 포함시키며, 모든 것을 서버 측에서 처리하는 것에서 벗어나고 있다. 예를 들어 최신 웹 애플리케이션에서는 동적 웹 페이지에 대한 비즈니스 로직이 JavaScript로 작성되어 클라이언트 측에서 실행된다.

클라이언트 측과 마찬가지로 **서버 측**은 클라이언트가 아닌 서버에서 일어나는 모든 일을 의미한다.

과거에는 거의 모든 비즈니스 로직이 서버 측에서 실행되었으며, 여기에는 동적 웹 페이지 렌더링, 데이터베이스와의 상호 작용, 신원 인증, 푸시 알림 등이 포함되었다.

![images_juejue_post_9274c6d8-b59a-4fab-a09c-37b985ee3f7a_image](https://github.com/inu-appcenter/server-study-16th/assets/86196038/0b7aea1f-61e0-43c9-b66b-ee78e29976b0)

위 사진은 웹에서 클라이언트 서버 모델의 기본이 되었던 형태이다. 

웹 브라우저(클라이언트)는 웹 서버에게 페이지를 요청하여 응답받은 페이지를 화면에 띄운다. 

또는 클라이언트에서 DBMS에 정보를 요청을 하며 응답 받은 결과를 사용한다. DBMS는 사용자들이 DB 내의 데이터에 접근할 수 있도록 도와주는 소프트웨어이다. DBMS는 보통 서버 형태로 서비스를 제공하기 때문에 위처럼 클라이언트 측에서 DBMS에 접근하여 동작하는 프로그램이 많이 만들어졌다.

하지만 이런 형태는 클라이언트 측에서 로직이 많아지고 프로그램의 크기가 커져 클라이언트 측 부담이 많아진다는 문제점이 있다. 게다가 로직이 변경될 때마다 프로그램을 매번 배포해주어야 했고, 로직이 클라이언트에 포함되어 있는 만큼 보안에도 문제가 있었다.

![images_juejue_post_d214b46d-e7f9-4376-ac66-44ca52dab960_image](https://github.com/inu-appcenter/server-study-16th/assets/86196038/c0022ba4-d5b8-49a5-a3e1-d791cc658873)

이런 이유로 **미들웨어**가 등장했다.

클라이언트와 DBMS 사이에 비즈니스 로직을 수행해주는 또 다른 서버, 미들웨어를 두어 클라이언트는 요청을 단순히 미들웨어로 보내면, 미들웨어는 클라이언트가 부담하던 로직을 수행하고, 데이터 조작이 필요하면 DBMS에 부탁한 뒤 결과를 클라이언트에게 전송하도록 하였다.

미들웨어의 등장으로 클라이언트는 더 이상 복잡한 로직을 담당할 필요가 없어졌고, 프로그램의 크기도 작아졌으며, 로직 수정 시 매번 배포해야 했던 번거로움을 해결했다.

미들웨어에는 2가지 종류가 있다.

- **웹 서버(Web Server)**
    
    ![images_juejue_post_55b55758-25f6-4889-8221-18c8d09ecd9b_image](https://github.com/inu-appcenter/server-study-16th/assets/86196038/9b680e53-ca76-4d35-8257-50b98c234d60)

    

WS(웹 서버)는 웹 브라우저 클라이언트로부터 HTTP 요청을 받아 정적인 컨텐츠(.html .jpeg .css 등)를 제공한다. 단순히 저장되어 있는 웹 페이지를 클라이언트로 전달하고, 클라이언트로부터 컨텐츠를 전달 받아 저장하거나 처리하는 역할을 담당한다.

WS의 예로는 Apache Server, Nginx IIS(Windows 전용 Web 서버) 등이 있다.

- **웹 어플리케이션 서버 (Web Application Server)**
    
    ![images_juejue_post_c70d1fe7-b139-4fb1-be74-0a0153ef9f5f_image](https://github.com/inu-appcenter/server-study-16th/assets/86196038/848a063b-5b4e-40d2-8d5e-d04cfecc53e8)
    

WAS(웹 어플리케이션 서버)는 DB 조회나 다양한 로직 처리를 요구하는 동적인 컨텐츠를 제공하기 위해 만들어진 Application Server이다. HTTP를 통해 컴퓨터나 장치의 어플리케이션을 수행해주는 미들웨어라고 볼 수 있다.

웹 서버 + 웹 컨테이너의 형태를 띄고 있고, 웹 서버 기능들을 구조적으로 분리하여 처리하고자 하는 목적으로 제시되었다.

WAS의 예로는 Tomcat, JBoss, Jeus, Web Sphere 등이 있다.

![img1 daumcdn](https://github.com/inu-appcenter/server-study-16th/assets/86196038/989d31ad-2169-46da-b543-554e0c6a1654)

<aside>
💡 **웹 서버와 웹 어플리케이션 서버는 아예 다른 것일까?**
그렇지 않다. 웹 어플리케이션 서버는 기본적으로 웹 서버의 기능을 제공한다.
대중적으로 사용되는 톰캣의 경우에도 자체적인 웹 서버가 충분한 기능을 제공하기 때문에 아파치 같은 웹 서버를 설치하지 않고도 사용할 수 있는 것이다.

**그렇다면 웹 서버는 필요 없는 것일까?**
WAS와 WS를 분리하여 웹 서버를 웹 어플리케이션 서버 앞에 두면 서버 장애 시 장애 극복에 쉽게 대응할 수 있다.  
****사람들이 많이 접속하는 대용량 웹 어플리케이션 서버의 경우 서버의 수가 여러 대일 수 있다. 만약 사용 중 WAS에 문제가 생겨 WAS를 재시작해야 하는 경우 WS에서 WAS를 사용하지 못하도록 요청을 차단한 후 WAS를 재시작한다면, 사용자들을 WAS에 문제가 발생한 지 모르고 이용이 가능하다.
또한 규모가 큰 서비스일수록 자원 이용의 효율성을 높이고, 배포 및 유지 보수의 편의성을 위해서라도 대체로 분리하여 둔다.
****

![images_juejue_post_edba2523-f45f-4e7d-80c8-1f4e21e6b04e_image](https://github.com/inu-appcenter/server-study-16th/assets/86196038/4ea64f96-a126-460d-b802-cb4ac5ec4f2e)

</aside>

# 자바 웹 프레임워크의 역사

### 웹 프레임워크(Web Framework)?

> ***웹 프레임워크**는 웹 개발 프로세스의 일부 측면을 자동화하여 더 쉽고 빠르게 만들 수 있는 소프트웨어 도구*
> 

웹사이트, 웹 어플리케이션, 모바일 앱 또는 소프트웨어의 아키텍처를 구축하는데 도움이 되는 모델을 말한다.

즉, 자바 웹 프레임워크는 자바 언어로 웹 구축을 쉽게 할 수 있도록 탄생한 도구이다. 대표적으로 Spring 프레임워크를 예로 들 수 있다.

그렇다면 자바 웹 프레임워크는 어떻게 생겨났고 어떻게 발전해왔을까? 그 과정을 알기 위해 웹의 등장부터 순서대로 알아보겠다.

### 웹의 시작

- **WWW (World-Wide-Web)**
    
    ![what-is-www](https://github.com/inu-appcenter/server-study-16th/assets/86196038/fdf79441-fbfe-4d27-83a7-be3050b7b088)

    

웹은 월드 와이드 웹 (WWW)을 짧게 줄여 부르는 말이다. 웹은 1989년, ‘팀 버너스 리’라는 사람이 당시 CERN(유럽 입자 물리 연구소)에서 근무하면서 핵 소립자 실험의 성과를 전 세계 연구자들과 쉽게 공유하고자 하는 니즈에서 시작되었다.

당대에도 이메일이나 파일 전송과 같은 기술은 존재했으나 그는 수많은 연구자를 상대로 일일이 메일을 보내는 방법이 번거롭다 여겼고 경영진에게 하이퍼텍스트(HyperText) 개념 소개와 함께 ‘정보 관리 네트워크’의 필요성을 주장하며 제안서를 제출한다. 이 제안서가 WWW의 개념적인 시초이다. 

> *하이퍼텍스트(HyperText)란 웹 페이지를 다른 웹 페이지로 연결할 수 있는 문서를 말한다. 이런 하이퍼텍스트의 등장으로 참조하고 있는 다른 페이지에 대한 열람이 쉬워졌고, 정보의 전달 효율은 크게 향상되었다.*
> 

팀 버너스 리는 이후 WWW의 실제 구현에 참여했고 구현 과정에서 웹의 핵심 기술들인 HTML과 HTTP 등이 고안되었다. 그리고 최초의 웹 서버로 불리는 CERN Httpd도 이 시점에 등장하였다.

- **CGI**

WWW에 대한 수요가 급속도로 늘어남에 따라 동적 페이지에 대한 요구 또한 증대되었다.

하지만 초기 웹 서버 모델은 URL에 맞춰 페이지를 반환하기만 할 뿐, 동적인 페이지를 생성할 능력이 없어 동적 페이지 생성을 위해서는 외부 프로그램의 도움이 필요했다.

따라서 HTTP 요청이 들어오면 그에 걸맞은 적절한 프로그램을 수행하자는 아이디어가 부상하게 되었고, CGI라는 표준 인터페이스가 등장하게 되었다.

> *CGI란, Common Gateway Interface (공용 게이트웨이 인터페이스)의 약자로 서버와 애플리케이션 간에 데이터를 주고받는 방식을 의미한다.*
> 

![2023-10-25-HISTORY-OF-WEB-APP_002](https://github.com/inu-appcenter/server-study-16th/assets/86196038/a3bc3b7d-79a9-44f5-ae2f-e4b9a0a97dcf)


웹 서버는 동적 페이지를 원하는 클라이언트의 요청이 들어오면 CGI 인터페이스를 통해 외부 프로그램을 실행시키면서 적절한 HTTP 응답을 반환할 수 있었다.

하지만 CGI 방식에는 몇 가지 문제점들이 존재했다. 

1. 요청마다 프로그램을 실행시켜야 해 서버 리소스가 많이 소모됨
2. HTTP 요청마다 스레드가 아닌 프로세스를 할당하는 구조이기에 서버 부하가 상당함.
3. CGI 프로그램이 C, C++, Perl 등의 언어로 작성되어 어플리케이션 확장이 어려움

이에 Java 진영에서 이런 문제점들을 해결할 수 있는 Servlet이라는 모델을 제시했다.

### 자바 웹

- **Servlet**

Servlet은 Java 코드를 사용해서 웹페이지를 동적으로 생성하는 기술을 의미한다. HTTP 요청이 들어오면 이에 대응하는 클래스의 메서드를 호출하는 방식을 갖고 있다.

이런 Servlet 방식은 CGI 방식이 요청마다 프로세스를 할당했던 것과는 달리 스레드를 할당하였기 때문에 상당히 경제적이었다.

구체적으로는 Java Application을 미리 띄워두기 때문에 매번 프로세스를 할당할 필요가 없다. HTTP 요청이 들어오면 Java Application에게 처리를 의뢰하고 Java Application은 스레드를 생성해 요청 처리에 적합한 Servlet을 실행한다.

또한 Servlet은 Java 진영의 기술이기 때문에 순수 Java로 작성되어 JVM 생태계에 친화적이고, Java의 특징 중 하나인 ‘플랫폼 독립성’을 가장 잘 누릴 수 있게 된다. 즉, Servlet을 한 번만 구현해 두면 어느 플랫폼에서도 쉽게 재사용할 수 있다.

Servlet의 HTTP 요청 처리 방식은 CGI 방식과 크게 다르진 않다.

![2023-10-25-HISTORY-OF-WEB-APP_004](https://github.com/inu-appcenter/server-study-16th/assets/86196038/b455fcf8-056c-4447-bbb8-d075be54339c)


HTTP 요청이 발생하고 웹 서버에 도달하면 웹 서버는 Servlet Container에게 요청을 전달한다. Servlet Container는 적절한 Servlet(Java 프로그램)을 선택하고 실행시킨다. Servlet은 요청에 걸맞은 동적 페이지를 반환한다.

> *Servlet Container란 Servlet을 관리해주면서 동적 웹페이지를 제공하기 위한 기타 작업을 수행해주는 역할을 한다. Servlet은 동적 페이지를 만들어내기 위한 프로그램에 불과하므로 이들을 초기화해주고 관리해주는 누군가가 필요하기 때문이다.*
> 

그렇지만 서블릿 방식의 웹 어플리케이션에도 문제점이 있었다. 자바 코드에서 HTML을 다루다보니 비즈니스 로직과 뷰의 영역이 분리되지 않고 모호하게 공존했다. 아래 Servlet 코드에서는 로그인 처리를 위한 코드와 렌더링을 위한 코드가 하나의 메서드에 존재한다.

```java
public class MyServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		String account = request.getHeader("account");
		String password = request.getHeader("password");
		
		// 비즈니스 로직
		validateCredential(account, password);

		// 뷰를 위한 로직
		out.write("<html>"); 
		out.write("<head> </head>");
		out.write("<body> Hello world! </body>");
		out.write("</html>");
	}
```

뷰와 비즈니스 로직의 수정 지점이 같다보니 유지 보수하는 데 어려움을 겪기 일쑤였다. 프로그래머는 Servlet 코드를 변경하고 싶어도 디자이너가 HTML 작업을 하고 있다면 기다려야 했다. 

따라서 Java 코드와 HTML 코드의 분리를 더 명확히 하자는 요구가 생겨냤고, JSP가 등장하게 된다.

- **JSP**

JSP는 Java Server Page의 약자로 (현재는 Jakarta Server Page라고도 불림) HTML 코드에 Java 코드를 넣어 동적 웹 페이지를 생성하는 기술을 의미한다.

아래 코드는 간단한 JSP 파일의 예시이다. <% %> 블록 안에 Java 코드가 포함되어 있다. <% %>와 같은 식별자들은 JSP 태그라고 하며, JSP 태그를 활용해 Java 코드를 HTML에 삽입할 수 있다.

```html
<HTML>
<HEAD><TITLE>The Welcome User JSP</TITLE></HEAD>
<BODY>
		<% String user=request.getParameter("user"); %>
		<H3>Welcome <%= (user==null) ? "" : user %>!</H3>
		<P><B> Today is <%= new java.util.Date() %>. Have a nice day! :-)</B></P>
		<B>Enter name:</B>
		<FORM METHOD=get>
				<INPUT TYPE="text" NAME="user" SIZE=15>
				<INPUT TYPE="submit" VALUE="Submit name">
		</FORM>
</BODY>
</HTML>
```

JSP를 사용하는 경우 프로그래머는 JSP 태그로 감싸진 부분만 다루면 되고, 디자이너는 JSP 태그 이외의 HTML만 다루면 되므로 순수 Servlet을 사용하는 방법에 비해 유지 보수성이 향상된다.

사실 위처럼 비즈니스 로직을 직접 JSP 페이지에 작성하기보다는 ‘Java Beans’를 사용해 작성하는 경우가 많았다. Java Beans는 재사용이 가능한 컴포넌트, Java 클래스를 의미한다.

아래 코드는 Java Beans를 활용해 JSP 프로그래밍을 한 예시이다. userBean, dataBean이라는 Java Bean을 활용해 렌더링에 필요한 정보를 가져온다.

```html
<HTML>
<HEAD><TITLE>The Welcome User JSP</TITLE></HEAD>
<BODY>
		<jsp:useBean id="userBean" class="mybeans.UserBean" scope="page" />
		<jsp:useBean id="dateBean" class="mybeans.DateBean" scope="page" />
		<% String user=request.getParameter("user"); %>
		<H3>Welcome <%= userBean.getName() %>!</H3>
		<P><B> Today is <%= dateBean.getDate() %>. Have a nice day! :-)</B></P>
		<B>Enter name:</B>
		<FORM METHOD=get>
				<INPUT TYPE="text" NAME="user" SIZE=15>
				<INPUT TYPE="submit" VALUE="Submit name">
		</FORM>
</BODY>
</HTML>
```

이런 JSP의 경우 어떤 방식으로 사용하느냐에 따라 모델 1 구조와 모델 2 구조로 나뉘어싿. JSP를 통해 뷰의 구성과 제어 로직의 처리까지 모두 수행한다면 모델 1이라고 불렸고, 뷰의 구성만 담당한다면 모델 2라고 불렸다.

앞서 본 Java Beans를 활용하는 JSP 예시 코드가 모델 1에 해당한다. JSP가 직접 Java Beans를 호출하며 제어 로직을 담당하고 있기 때문이다.

![2023-10-25-HISTORY-OF-WEB-APP_005](https://github.com/inu-appcenter/server-study-16th/assets/86196038/2bf467fd-320f-49f2-8656-dab44a9c38f1)

모델 1의 경우 구조가 단순하여 간단한 페이지를 구성할 떄 주로 사용되었지만 규모가 큰 프로그램을 작성할 때에는 상당한 양의 Java 코드가 JSP 파일에 드러나는 점 때문에 JSP의 장점을 살리지 못했다. 

따라서 대안으로 모델 2가 등장하게 된다. 모델 2의 경우 모델 1을 보완하여 제어 로직 처리는 서블릿에게 넘기고 JSP는 렌더링만 담당하는 방식을 갖고 있다. 이를 통해 비즈니스 로직과 뷰의 책임을 명확하게 분리할 수 있었다.

코드로 나타내면 아래와 같다. 서블릿에서 HTTP 요청을 받고, 적절한 제어 로직을 수행한다. 그리고 request의 Attribute에 렌더링을 원하는 객체를 등록한 뒤, JSP 페이지로 포워딩한다.

```java
// Servlet 코드
public class MyServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 비즈니스 로직 수행
        java.util.Date now = new java.util.Date();
        request.setAttribute("currentDateTime", now);

        // JSP 페이지로 포워딩
        request.getRequestDispatcher("myView.jsp")
					.forward(request, response);
    }
}
```

그리고 JSP 페이지에서는 단순히 currentDateTime 객체를 꺼내 렌더링만 수행한다.

```html
<!-- JSP 코드 -->
<HTML>
<HEAD><TITLE>JSP Model 2 example</TITLE></HEAD>
<BODY>
    <H1>current date and time:</H1>
    <P><%= request.getAttribute("currentDateTime") %></P>
    <h2>login form:</h2>
    <FORM action="process.jsp" method="post">
        <INPUT type="text" name="name"><BR>
        <INPUT type="text" name="email"><BR>
        <INPUT type="submit" value="submit">
    </FORM>
</BODY>
</HTML>
```

이처럼 모델 2는 Servlet과 JSP의 관심사를 분리하여 유지 보수성을 향상시킨 모델이다. Servlet은 제어 로직을 담당하고, JSP는 뷰 로직을 담당한다. 그리고 이 구조는 현재 우리에게 익숙한 MVC 패턴과 유사하다.

- **J2EE와 EJB**
    
    ![2023-10-25-HISTORY-OF-WEB-APP_006](https://github.com/inu-appcenter/server-study-16th/assets/86196038/37ab896c-1dcc-452f-9186-3f1e64704484)
    

앞서 동적 페이지 구현을 위해 살펴본 Servlet과 JSP는 사실 Java EE에 포함되어있다.  J2EE라고도 불리며 기업 환경의 어플리케이션을 구성하는 데 필요한 표준의 집합을 의미한다. 

<aside>
💡

**Java EE(Enterprise Edition)**
- Java EE 플랫폼은 Java SE 플랫폼을 기반으로 그 위에 탑재됨
- 웹 프로그래밍에 필요한 기능을 다수 포함
  (JSP, Servlet, JDBC, JNDI, JTA 등)
- 대규모, 다계층, 확장성, 신뢰성, 보안 네트워킹 API 등을 제공

**Java SE(Standard Edition)**
- 가장 대중적인 자바 플랫폼
- 흔히 자바 언어라고 하는 대부분의 패키지가 포함된 에디션
- Java SE의 API는 자바 프로그래밍 언어의 핵심 기능들을 제공
- 가상머신, 개발도구, 배포기술, 부가 클래스 라이브러리, 툴킷 등 제공

</aside>

J2EE에는 EJB라는 기술이 존재한다. EJB(Enterprise Java Beans)란 분산 애플리케이션을 지원하는 컴포넌트 객체이며, J2EE를 사용해 엔터프라이즈 급 서버를 개발하는 경우 EJB로 비즈니스 로직을 처리하였다. 그리고 이런 EJB 객체들을 관리하는 EJB 컨테이너라는 것도 존재했는데, EJB 컨테이너는 분산 트랜잭션 지원 등의 기술적인 지원을 해주었다.

하지만 EJB로부터 기술적인 지원을 받기 위해서는 EJB 컨테이너에 종속적인 코드들을 많이 작성해야 했고, 떄문에 순수한 비즈니스 로직을 작성하기 어려웠다.

다음은 EJB를 사용하는 경우의 예시 코드이다. HelloBean은 단순히 이름이 입력으로 들어왔을 때 “Hello”를 붙여 출력해주는 비즈니스 객체이다. 단순한 비즈니스 로직 수행임에도 불구하고 SessionBean이라는 인터페이스를 구현해야 하고 EJB와 관련된 코드가 상당 부분 차지하고 있는 모습을 확인할 수 있다.

```java
import javax.ejb.*;

public class HelloBean implements SessionBean {

    public String sayHello(String myName) throws EJBException {
        return ("Hello " + myName);
    }

    /* ------------------------------------------------------
    * Begin EJB-required methods. The following methods are called
    * by the container, and never called by client code
    * ------------------------------------------------------- */

    public void ejbCreate() throws CreateException {
        // when bean is created
    }

    public void setSessionContext(SessionContext ctx) {

    }

		// Life Cycle Methods

    public void ejbActivate() {

    }

    public void ejbPassivate() {

    }

    public void ejbCreate() {

    }

    public void ejbRemove() {

    }
}
```

이런 형식의 웹 어플리케이션 개발은 유지 보수성에 있어 많은 문제를 불러왔고, 흔히 이 시기를 ‘추운 겨울’이라고 표현하기도 하였다.

- **Spring 프레임워크의 탄생**

J2EE 표준을 준수하는 어플리케이션들이 너무나 복잡해지니 로드 존슨이라는 사람이 ‘J2EE Development without EJB’ 라는 책을 출간하게 된다.

![2023-10-25-HISTORY-OF-WEB-APP_007](https://github.com/inu-appcenter/server-study-16th/assets/86196038/5a77b4d3-c70b-4a0a-a6ca-c471e21977c2)

로드 존슨은 해당 저서에서 EJB의 문제점을 제시하면서 EJB를 사용하지 않고도 충분히 고품질의 어플리케이션을 개발할 수 있음을 증명했다. 이 저서가 바로 Spring이라는 웹 프레임워크의 시작점이 되었다.

![image](https://github.com/inu-appcenter/server-study-16th/assets/86196038/4d289e86-3716-4ca9-a2c9-a6f6eaa69313)

로드 존슨이 주장하는 Spring의 핵심 가치는 ‘POJO를 통해 순수한 비즈니스 로직을 작성하자’이다. Spring은 EJB의 문제점을 해결하기 위한 장치들을 여럿 가지고 있다. 대표적으로는 제어의 역전(IoC), 의존성 주입(DI), 관점 지향 프로그래밍(AOP), 일관된 서비스 추상화(PSA)가 있다.

이런 Spring을 사용하면 EJB 스펙을 따르지 않아도 EJB와 비슷한 효과를 낼 수 있다. 여기서 주의할 점은 Spring 자체가 J2EE 사양을 대체하기 위한 프레임워크는 아니라 J2EE 사양을 보완하기 위해 등장한 프레임워크라는 것이다. 하지만 현재 Spring은 단순히 J2EE 사양을 보완할 뿐만 아니라 Spring MVC, Spring Batch, Spring Security와 같은 많은 기술이 생겨나며 독자적인 생태계를 구성하고 있다.