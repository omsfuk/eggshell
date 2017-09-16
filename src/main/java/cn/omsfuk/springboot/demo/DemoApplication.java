package cn.omsfuk.springboot.demo;

import cn.omsfuk.springboot.demo.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@SpringBootApplication
@Controller
public class DemoApplication {

    @Autowired
    private ServerService serverService;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @RequestMapping(value = "admin", method = RequestMethod.GET)
    public String admin(Model model) {
        model.addAttribute("terminated", serverService.getServerStatus());
        return "admin";
    }

    @RequestMapping(value = "start", method = RequestMethod.POST)
    public String start(@RequestParam(defaultValue = "65520") int browserPort,
                       @RequestParam(defaultValue = "65522") int schedulerPort,
                       Model model) {
        serverService.startServer(browserPort, schedulerPort);
        model.addAttribute("terminated", serverService.getServerStatus());
        return "redirect:admin";
    }

    @RequestMapping(value = "stop", method = RequestMethod.GET)
    public String stop(Model model) {
        serverService.stopServer();
        model.addAttribute("terminated", serverService.getServerStatus());
        return "redirect:admin";
    }

    @RequestMapping(value ="startClient", method = RequestMethod.POST)
    public String startClient(@RequestParam(defaultValue = "localhost") String tomcatHost,
                              @RequestParam(defaultValue = "8080") int tomcatPort,
                              @RequestParam(defaultValue = "localhost") String serverHost,
                              @RequestParam(defaultValue = "65521") int serverPort,
                              @RequestParam(defaultValue = "localhost") String schedulerHost,
                              @RequestParam(defaultValue = "65522") int schedulerPort) {
        serverService.startClient(tomcatHost, tomcatPort, serverHost, serverPort, schedulerHost, schedulerPort);
        return "redirect:admin";
    }
}
