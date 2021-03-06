package com.example.ivan.loginapp.rest;

import android.util.Log;

import com.example.ivan.loginapp.Group;
import com.example.ivan.loginapp.User;
import com.example.ivan.loginapp.activity.Security;
import com.example.ivan.loginapp.rest.url.URLGroupService;
import com.example.ivan.loginapp.rest.url.URLUserService;
import com.example.ivan.loginapp.rest.url.URLWebService;


import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;


public class Connection {
    private HttpHeaders headers;
    private RestTemplate rest;

    public Connection() {
        headers = createHeaders("administrator", "hardpassword");
        rest = new RestTemplate(getClientRequestFactory());
        rest.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }

    private ClientHttpRequestFactory getClientRequestFactory() {

        int timeout = 4000;
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setReadTimeout(timeout);
        clientHttpRequestFactory.setConnectTimeout(timeout);
        return clientHttpRequestFactory;
    }

    private HttpHeaders createHeaders(String username, String password) {

        HttpAuthentication httpAuthentication =
                new HttpBasicAuthentication(username, password);
        HttpHeaders headers = new HttpHeaders();
        headers.setAuthorization(httpAuthentication);
        return headers;
    }

    public Group[] getGroups() {

        HttpEntity<Group[]> request = new HttpEntity<>(headers);
        Group[] groups = rest.exchange(URLWebService.URL + URLGroupService.URL_GROUPS_BY_POSITION + "?id=3", HttpMethod.GET, request, Group[].class).getBody();
        return groups;
    }

    public User[] getUsers() {
        HttpEntity<User[]> request = new HttpEntity<>(headers);
        User[] users = rest.exchange(URLWebService.URL + URLUserService.URL_USERS, HttpMethod.GET, new HttpEntity<User[]>(headers), User[].class).getBody();
        return users;
    }

    public User registUser(User user) {
        HttpEntity<User> request = new HttpEntity<>(user, headers);
        User newuser = rest.exchange(URLWebService.URL + URLUserService.URL_ADD, HttpMethod.POST, request, User.class).getBody();
        return newuser;

    }
    public User signIn(String login, String password) {
        User user = null;
        try {
            HttpEntity<User> request = new HttpEntity<>(headers);
            user = rest.exchange(URLWebService.URL + URLUserService.URL_LOGIN + "?login=" + login, HttpMethod.GET, request, User.class).getBody();
            if (user != null) {
                String passUser = user.getPassword();
                passUser = Security.decryptPass(passUser);
                if (!passUser.equals(password))
                    user = null;
            }
        }
        catch (Exception e)
        {
            Log.e("errorC",e.getMessage());
            e.printStackTrace();
    }
        return user;

    }

}
