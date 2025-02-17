package hello.container;

import hello.spring.HelloConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class AppInitV2Spring implements AppInit{
    @Override
    public void onStartup(ServletContext servletContext) {

        System.out.println("AppInitV2Spring.onStartup");

        //스프링 컨테이너 생성, 스프링 빈 등록 적용
        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
        appContext.register(HelloConfig.class);

        //스프링 MVC 디스패처 서블릿 생성, 스프링 컨테이너 연결
        DispatcherServlet dispatcher = new DispatcherServlet(appContext);

        //디스패처 서블릿 서블릿 컨테이너에 등록 (이름은 중복 방지를 위해서 V2로 했음)
        ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcherV2", dispatcher);

        // /spring/* 로 들어오는 요청이 디스패처 서블릿을 통해서 들어오도록 설정
        servlet.addMapping("/spring/*");
    }
}
