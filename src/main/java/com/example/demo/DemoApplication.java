package com.example.demo;

import javafx.application.Application;
import org.aspectj.weaver.ast.Or;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.util.List;

import static java.net.URLEncoder.encode;


@SpringBootApplication
public class DemoApplication {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RepoRepository repoRepository;

    @Autowired
    CollaboratorsRepository collaboratorsRepository;

	private  static final Logger log = LoggerFactory.getLogger(DemoApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        return builder.build();
    }

    private static void getUsingToken(){
        RestTemplate restTemplate = new RestTemplate();
        String token = "yourtokenhere";
	}


    @Bean
    public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
        return (String... args) -> {


            String token = "yourtokenhere";

            User user = restTemplate.getForObject("https://api.github.com/users/bilu-Blen?access_token="+token,  User.class);

            log.info(user.toString());
            user.setRepos_url(user.getRepos_url());
            userRepository.save(user);


 /*These work to get  the number of followers on following
       //following
        System.out.println(user.getFollowing());
        //followers
        System.out.println(user.getFollowers());*/

            //since it is an array what is returned use this method
            ResponseEntity<List<Repo>> repoResponse =
                    restTemplate.exchange(user.getRepos_url()+"?access_token="+token,
                            HttpMethod.GET,null, new ParameterizedTypeReference<List<Repo>>() {
                            });
            List<Repo> repos = repoResponse.getBody();

            int count= 0;
            for (Repo repo1 : repos) {
                repoRepository.save(repo1);

/*these get methods can be used on the repo to get the repo name, and forks
               log.info(repo1.getName());
                log.info(repo1.getPulls_url());
                System.out.println(repo1.getForks());
*/
    //taking out the last part https://api.github.com/repos/bilu-Blen/Arrays/pulls{/number}
                String str = repo1.getPulls_url();
                int index = str.lastIndexOf('{');
                str = str.substring(0,index);

    //getting pull request numbers
                ResponseEntity<List<Pull>> pullResponse =
                        restTemplate.exchange(str+"?access_token="+ token,
                                HttpMethod.GET, null, new ParameterizedTypeReference<List<Pull>>() {
                                });
                List<Pull> pulls = pullResponse.getBody();
//number of pulls
                for(Pull pull :pulls){
                    count = count + 1;
                }
                log.info(repo1.getName());

                System.out.println("This is the count of pulls for this repo " + count);

    // getting collaborators
                String collaborators = repo1.getCollaborators_url();
                //taking out the last part after { in, https://api.github.com/repos/bilu-Blen/ATMApp/collaborators{/collaborator}
                index = collaborators.lastIndexOf('{');
                collaborators = collaborators.substring(0, index);

                ResponseEntity<List<Collaborators>> collaboratorsresponse =
                restTemplate.exchange(collaborators + "?access_token=" + token,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Collaborators>>() {
                        });

                List<Collaborators> collaboratorsList = collaboratorsresponse.getBody();

                for(Collaborators collaborator : collaboratorsList){
                    System.out.println("The collaborator/s is/are " + collaborator.getLogin());
                    collaboratorsRepository.save(collaborator);


                }
            }






// getting members of a company

          /*  Orgs orgs = restTemplate.getForObject("https://api.github.com/orgs/github", Orgs.class);
            log.info(orgs.toString());
            log.info(orgs.getLogin().toString());
            log.info(orgs.getMembers_url().toString());*/

            //                    restTemplate.getForObject("https://api.github.com/orgs/github/members", Orgs.class);

            /*ResponseEntity<List<User>> memberResponse =
                    restTemplate.exchange("https://api.github.com/orgs/github/members",
                            HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {
                            });
            List<User> users = memberResponse.getBody();
            log.info(users.toString());
            for(User user1: users){
                log.info(user1.getLogin());*/

            //}


        };
    }

}
