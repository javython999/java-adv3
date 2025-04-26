# 람다가 필요한 이유
## 1. 람다가 필요한 이유1
```java
public class Ex0Main {

    public static void helloJava() {
        System.out.println("프로그래밍 시작");
        System.out.println("Hello Java");
        System.out.println("프로그램 종료");
    }

    public static void helloSpring() {
        System.out.println("프로그래밍 시작");
        System.out.println("Hello Spring");
        System.out.println("프로그램 종료");
    }

    public static void main(String[] args) {
        helloJava();
        helloSpring();
    }
}
```
코드의 중복이 보일 것이다. 
코드를 리팩토링해서 코드의 중복을 제거해보자.
`helloJava()`, `helloSpring()`
메서드를 하나로 통합하면 된다.

```java
public class Ex0RefMain {

    public static void hello(String string) {
        System.out.println("프로그래밍 시작");
        System.out.println(string);
        System.out.println("프로그램 종료");
    }
    
    public static void main(String[] args) {
        hello("Hello Java");
        hello("Hello Spring");
    }
}
```
프로그래밍에서 중복을 제거하고, 좋은 코드를 유지하는 핵심은 변하는 부분과 변하지 않는 부분을 분리하는 것이다.
여기서 핵심은 변하는 부분을 메서드 내부에서 가지고 있는 것이 아니라, 외부에서 전달 받는다는 점이다.

### 값 매개변수화(Value Parameterization)
여기서 변하는 부븐은 `Hello Java`, `Hello Spring` 같은 문자값(Value)이다.
문자값, 숫자값처럼 구체적인 값을 메서드(함수) 안에 두는 것이 아니라, 매개변수(파라미터)를 통해 외부에서 전달 받도록 해서,
메서드의 동작을 달리하고, 재사용성을 높이는 방법을 값 매개변수화(Value Parameterization)라 한다.

## 2. 람다가 필요한 이유2

```java
public class Ex1Main {

    public static void helloDice() {
        long startNs = System.nanoTime();
        //코드 조각 시작
        int randomValue = new Random().nextInt(6) + 1;
        System.out.println("주사위 = " + randomValue);
        //코드 조각 종료
        long endNs = System.nanoTime();
        System.out.println("실행 시간: " + (endNs - startNs) + "ns");
    }

    public static void helloSum() {
        long startNs = System.nanoTime();
        //코드 조각 시작
        for (int i = 1; i <= 3; i++) {
            System.out.println("i = " + i);
        }
        //코드 조각 종료
        long endNs = System.nanoTime();
        System.out.println("실행 시간: " + (endNs - startNs) + "ns");
    }

    public static void main(String[] args) {
        helloDice();
        helloSum();
    }
}
```
하나의 메서드에서 실행할 수 있도록 리팩토링 해보자.

```java
public interface Procedure { 
    void run();
}
```
```java
public class Ex1RefMain {

    public static void hello(Procedure procedure) {
        long startNs = System.nanoTime();
        procedure.run();
        long endNs = System.nanoTime();
        System.out.println("실행 시간: " + (endNs - startNs) + "ns");
    }

    static class Dice implements Procedure {
        @Override
        public void run() {
            int randomValue = new Random().nextInt(6) + 1;
            System.out.println("주사위 = " + randomValue);
        }
    }

    static class Sum implements Procedure {
        @Override
        public void run() {
            for (int i = 1; i <= 3; i++) {
                System.out.println("i = " + i);
            }
        }
    }

    public static void main(String[] args) {
        hello(new Dice());
        hello(new Sum());
    }

}
```
### 어떻게 외부에서 코드 조각을 전달할 수 있을까?
코드 조각은 보통 메서드(함수)에 정의한다.
따라서 코드 조각을 전달하기 위해서는 메서드가 필요하다.
인스턴스를 전달하고 인스턴스에 있는 메서드를 호출하면 된다.

이 문제를 해결하귀 위해 인터페이스를 정의하고 구현 클래스를 만들었다.

```java
public interface Procedure { 
    void run();
}

class Dice implements Procedure {
    @Override
    public void run() {
        int randomValue = new Random().nextInt(6) + 1;
        System.out.println("주사위 = " + randomValue);
    }
}

class Sum implements Procedure {
    @Override
    public void run() {
        for (int i = 1; i <= 3; i++) {
            System.out.println("i = " + i);
        }
    }
}
```

### 정리
* 값 매개변수화
  * 값을 바꿔가며 메서드(함수)의 동작을 달리함.
* 동작 매개변수화
  * 어떤 동작(로직)을 수행할지를 메서드(함수)에 전달(인스턴스 참조, 람다 등)

## 람다가 필요한 이유3

```java
// 익명 클래스 사용1
public static void main(String[] args) {
    Procedure dice = new Procedure() {
        @Override
        public void run() {
            int randomValue = new Random().nextInt(6) + 1;
            System.out.println("주사위 = " + randomValue);
        }
    };

    Procedure sum = new Procedure() {
        @Override
        public void run() {
            for (int i = 1; i <= 3; i++) {
                System.out.println("i = " + i);
            }
        }
    };

    hello(dice);
    hello(sum);
}

// 익명 클래스 사용2
public static void main(String[] args) {
    Procedure dice = new Procedure() {
        @Override
        public void run() {
            int randomValue = new Random().nextInt(6) + 1;
            System.out.println("주사위 = " + randomValue);
        }
    };

    Procedure sum = new Procedure() {
        @Override
        public void run() {
            for (int i = 1; i <= 3; i++) {
                System.out.println("i = " + i);
            }
        }
    };

    hello(dice);
    hello(sum);
}
```
```java
// 람다 사용
public static void main(String[] args) {
    
    hello(() -> {
        int randomValue = new Random().nextInt(6) + 1;
        System.out.println("주사위 = " + randomValue);
    });

    hello(() -> {
        for (int i = 1; i <= 3; i++) {
            System.out.println("i = " + i);
        }
    });
}
```
람다를 사용한 코드를 보면 클래스나 인스턴스를 정의하지 않고, 매우 간편하게 코드블럭을 직접 정의하고, 전달하는 것을 확인할 수 있다.

## 함수 VS 메서드
#### Java
```java
public class Calculator {
    public int add(int x, int y) {
        return x + y;
    }
}

// 사용 예
Calculator cal = new Calculator();
int result = cal.add(2, 3);  // 'add'는 메서드
```
#### Python
```python 
# 함수: 클래스 밖에서 독립적으로 정의
def add(x, y):
  return x + y

# 메서드: 클래스(객체) 내부에 정의
class Calculator:
  def add(self, x, y):
    return x + y

# 사용 예
print(add(2, 3))  # 함수 호출
cal = Calculator()
print(cal.add(2, 3))  # 메서드 호출
```
#### 객체(클래스)와의 관계
* 함수(Function)
  * 독립적으로 존재하며, 클래스(객체)와 직접적인 연관이 없다.
  * 객체 지향 언어가 아닌 C등의 절차적 언어에서는 모든 로직이 함수 단위로 구성된다.
  * 객체지향 언어라 하더라도 Python, Javascript처럼 클래스 밖에서도 정의할 수 있는 "함수" 개념을 지원하는 경우, 이를 함수라고 부른다.
* 메서드(Method)
  * 클래스(또는 객체)에 속해있는 "함수"이다.
  * 객체의 상태(필드, 프로퍼티 등)에 직접 접근하거나, 객체가 제공해야 할 기능을 구현할 수 있다.
  * Java, C++, C#, Python 등 대부분의 객체지향 언어에서 클래스 내부에 정의된 함수는 보통 "메서드"라고 부른다.

#### 호출 방식과 스코프
* 함수(Function)
  * 호출 시에 객체 인스턴스가 필요 없다.
  * 보통 `이름(매개변수)` 형태로 호출된다.
  * 지역 변수, 전역 변수 등과 함께 동작하며, 클래스나 객체 특유의 속성(인스턴스 변수 등)은 다루지 못한다.
* 메서드(Method)
  * 보통 `객체(인스턴스).메서드이름(매개변수)` 행태로 호출한다.
  * 호출될 때, 해당 객체의 필드(속성)나 다른 메서드에 접근 가능하며, 이를 이용해 로직을 수행한다.
  * 인스턴스 메서드, 클래스(정적) 메서드, 추상 메서드 등 다양한 형태가 있을 수 있다.

#### 정리
* 메서드는 기본적으로 클래스(객체) 내부의 함수를 가리키며, 객체의 상태와 밀접한 관련이 있다.
* 함수는 클래스(객체)와 상관없이, 독립적으로 호출 가능한 로직의 단위이다.
* 메서드는 객체지향에서 클래스 안에 정의하는 특별한 함수라고 생각하면 된다.

## 람다 시작
#### 매개변수가 없는 익명 클래스 예제
```java
public static void main(String[] args) {
    Procedure procedure = new Procedure() {
        @Override
        public void run() {
            System.out.println("hello! lambda");
        }
    };

    procedure.run();
}

public static void main(String[] args) {
  Procedure procedure = () -> {
    System.out.println("hello! lambda");
  };

  procedure.run();
}
```
* 람다는 `() -> {}`와 같이 표현한다. `()` 부분이 메서드의 매개변수라 생각하면 되고, `{}` 부분이 코드 조각이 들어가는 본문이다.
* 람다를 사용할 때는 이름, 반환 타입은 생략하고, 매개변수와 본문만 간단하게 적으면 된다.
  * `(매개변수) -> {본문}`, 여기서는 매개변수가 없으므로 `() -> {본문}`
* 익명 클래스를 사용하는 것보다 람다를 사용할 때 코드가 훨씬 간결한 것을 확인할 수 있다.

#### 매개변수가 있는 익명 클래스 예제
```java
public static void main(String[] args) {
    MyFunction myfunction = new MyFunction() {
        @Override
        public int apply(int a, int b) {
            return a + b;
        }
    };

    int apply = myfunction.apply(1, 2);
    System.out.println("apply = " + apply);
}

public static void main(String[] args) {
    MyFunction myfunction = (int a, int b) -> {
      return a + b;
    };
  
    int apply = myfunction.apply(1, 2);
    System.out.println("apply = " + apply);
}
```
* 이번에는 매개변수가 있으므로 `(int a, int b) -> {본문}`과 같이 작성하면 된다.

# 람다
## 람다 정의
* 자바 8부터 도입된 람다는 자바에서 함수형 프로그래밍을 지원하기 위한 핵심 기능이다.
* 람다는 익명 함수이다. 따라서 이름 없이 함수를 표현한다.

#### 메서드나 함수는 다음과 같이 표현한다.
```java
반환타입 메서드명(매개변수) {
    본문
}
```
#### 람다는 다음과 같이 간결하게 표현한다.
```java
(매개변수) -> {본문}
```

#### 람다는 표현이 간결하다.
```java
Procedure procedure = new Procedure() {
    @Override
    public void run() {
        System.out.println("hello! lambda");
    }
};

Procedure procedure = () -> {
  System.out.println("hello! lambda");
};
```

#### 람다는 변수처럼 다룰 수 있다.
```java
Procedure procedure = () -> {
  System.out.println("hello! lambda");
};

procedure.run(); // 변수를 통해 람다를 실행
```

#### 람다도 클래스가 만들어지고, 인스턴스가 생성된다.
```java
public class InstanceMain {

    public static void main(String[] args) {
        Procedure procedure1 = new Procedure() {
            @Override
            public void run() {
                System.out.println("hello! lambda");
            }
        };

        System.out.println("class.class = " + procedure1.getClass());
        System.out.println("class.instance = " + procedure1);


        Procedure procedure2 = () -> {
            System.out.println("hello! lambda");
        };

        System.out.println("lambda.class = " + procedure2.getClass());
        System.out.println("lambda.instance = " + procedure2);
    }
}
```
```
class.class = class lambda.lambda.lambda1.InstanceMain$1
class.instance = lambda.lambda.lambda1.InstanceMain$1@1d81eb93
lambda.class = class lambda.lambda.lambda1.InstanceMain$$Lambda/0x000001d0c4003618
lambda.instance = lambda.lambda.lambda1.InstanceMain$$Lambda/0x000001d0c4003618@34a245ab
```
* 익명 클래스의 경우 `$`로 구분하고 뒤에 숫자가 붙는다.
* 람다의 경우 `$$`로 구분하고 뒤에 복잡한 문자가 붙는다.

정리
* 람다를 사용하면 익명 클래스 사용의 보일러플레이트 코드를 크게 줄이고, 간결한 코드로 생산성과 가독성을 높일 수 있다.
* 대부분의 익명 클래스는 람다로 대체할 수 있다.
* 람다를 사용할 때 `new` 키워드를 사용하지 않지만, 람다도 익명 클래스처럼 인스턴스가 생성된다.


