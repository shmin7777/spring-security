## spring security는 csrf를 기본적으로 enable시킨다.  

Spring Security 는 default로 헤더에  Cache-Control 를 추가하고,  no-cashe, no-store 등의 옵션을 추가 합니다.

 

이는 브라우저에게 '응 쿠키 못써' 라고 알려주는 역할을 합니다.

그러니까 쿠키를 사용하려면 Cache-Control 라는 헤더를 없애주어야 합니다.

![image](https://user-images.githubusercontent.com/67637716/227779160-a54829f0-a010-4f7b-af00-8a739de7f214.png)  


쿠키를 쓰는 방식은 보안에 취약하여 이제는 쿠키 없이 보안설정을 한다고 합니다. 크롬이 그러한 노력을 많이 하는 것 같습니다.

Spring Security 또한 이러한 추세에 맞춰 default 로 Cache-Control 에 옵션을 추가하지 않았나 추측합니다.

https://developer-ping9.tistory.com/234    
https://velog.io/@woohobi/Spring-security-csrf%EB%9E%80  

## chat gpt
![image](https://user-images.githubusercontent.com/67637716/227780959-c6de48a2-085d-4e63-aa2d-0bcd2b4c49af.png)  
![image](https://user-images.githubusercontent.com/67637716/227780974-0eb3a467-750f-4973-acea-964638e3991f.png)  
