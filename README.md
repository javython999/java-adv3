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


## 함수형 인터페이스
* 함수형 인터페이스는 정확히 하나의 추상 메서드를 가지는 인터페이스를 말한다.
* 람다는 추상 메서드가 하나인 함수형 인터페이스에만 할당할 수 있다.
* 단일 추상 메서드를 줄여서 SAM(Single Abstract Method)이라 한다.
* 람다는 클래스, 추상 클래스에는 할당할 수 없다. 오직 단일 추상 메서드를 가지는 인터페이스에만 할당할 수 있다.

```java
public interface NotSamInterface {
  void run();
  void go();
}
```
* 인터페이스의 메서드는 앞에 `abstract`(추상)가 생략되어 있다.
* `run()`, `go()` 두 개의 추상 메서드가 선언되 있다.
* 단일 추상 메서드(SAM)가 아니다. 이 인터페이스에는 람다를 할당할 수 없다.

```java
public interface SamInterface {
    void run();
}
```
* `run()` 한 개의 추상 메서드만 선언되어 있다.
* 단일 추상 메서드(SAM)이다. 이 인터페이스에는 람다를 할당할 수 있다.

```java
public static void main(String[] args) {
    SamInterface samInterface = () -> {
        System.out.println("sam");
    };

    samInterface.run();

    //컴파일 오류
    /*
    NotSamInterface notSamInterface = () -> {
        System.out.println("not sam");
    };
    */
    
}
```
* 람다는 하나의 함수이다. 따라서 람다를 인터페이스에 담으려면 하나의 메서드(함수) 선언만 존재해야 한다.
* 인터페이스는 여러 메서드(함수)를 선언할 수 있다.
* 이 함수를 `NotSamInterface`에 있는 `run()`, `go()` 둘 중에 하나에 할당해야하는 문제가 발생한다.

자바는 이러한 문제를 해결하기 위해, 단 하나의 추상메서드(SAM)만을 포함하는 함수형 인터페이스에만 람다를 할당할 수 있도록 제한했다.

### @FunctionalInterface
단 하나의 추상 메서드만을 포함한다는 것을 어떻게 보장할 수 있을까?
`@FunctionalInterface` 애노테이션을 붙여주면 된다. 이 애노테이션이 있으면 단 하나의 추상 메서드가 아니면
컴파일 단계에서 오류가 발생한다.

```java
@FunctionalInterface
public interface SamInterface {
    void run();
}
```

## 람다와 시그니처
람다를 함수형 인터페이스에 할당할 때는 메서드의 형태를 정의하는 요소인 메서드 시그니처가 일치해야 한다.
메서드 시그니처의 주요 구성 요소는 다음과 같다.

1. 메서드 이름
2. 매개변수의 수와 타입(순서 포함)
3. 반환 타입

람다는 익명 함수이므로 시그니처에서 이름은 제외하고, 매개변수, 반환 타입이 함수형 인터페이스에 선언한 메서드와 맞아야 한다.

## 람다와 생략
람다는 간결한 코드 작성을 위해 다양한 문법 생략을 지원한다.

#### 단일 표현식
```java
public static void main(String[] args) {

    // 기본
    MyFunction function1 = (int a, int b) -> {
        return a + b;
    };
    System.out.println("function1: " + function1.apply(1, 2));

    // 단일 표현식인 경우 중괄호와 리턴 생략 가능
    MyFunction function2 = (int a, int b) -> a + b;
    System.out.println("function2: " + function2.apply(1, 2));

    // 단일 표현식이 아닐 경우 중괄호와 리턴 모두 필수
    MyFunction function3 = (int a, int b) -> {
        System.out.println("람다 실행");
        return a + b;
    };
    System.out.println("function3: " + function3.apply(1, 2));
}
```
```java
public static void main(String[] args) {

    Procedure procedure1 = () -> {
        System.out.println("hello lamda");
    };
    procedure1.run();

    // 단일 표현식은 중괄호 생략 가능
    Procedure procedure2 = () -> System.out.println("hello lamda");
    procedure2.run();
    
}
```

#### 타입 추론
```java
MyFunction function1 = (int a, int b) -> a + b;
```
* 함수형 인터페이스인 `MyFunction`의 `apply()` 메서드를 보면 이미 `int a, int b`로 매개변수의 타입이 정의되어 있다.
* 따라서 이 정보를 사용하면 람다의 `(int a, int b)`에서 타입정보를 생략할 수 있다.

```java
public static void main(String[] args) {
        
    // 타입 생략전
    MyFunction myFunction1 = (int a, int b) -> a + b;
    
    
    // 타입 추론
    MyFunction myFunction2 = (a, b) -> a + b;
}
```

#### 매개변수의 괄호 생략
```java
public static void main(String[] args) {
    // 기본
    MyCall call1 = (int value) -> value * 2;
    
    // 타입 추론
    MyCall call2 = (value) -> value * 2;
    
    // 매개변수 괄호 생략 (매개 변수가 1개일 때)
    MyCall call3 = value -> value * 2;
}

interface MyCall {
    int call(int value);
}
```
* 매개변수가 정확히 하나이면서, 타입을 생략하고, 이름만 있는 경우 소괄호`()`를 생략할 수 있다.
* 매개변수가 없는 경우에는 `()`가 필수이다.
* 매개변수가 둘 이상이면 `()`가 필수 이다.

정리
* 매개변수 타입: 생략 가능하지만 필요하다면 명시적으로 작성할 수 있다.
* 반환 타입: 문법적으로 명시할 수 없고, 식의 결과를 보고 컴파일러가 항상 추론한다.
* 람다는 보통 간략하게 사용하는 것을 권장한다.
  * 단일 표현식이면 중괄호와 리턴을 생략하자.
  * 타입 추론을 통해 매개변수의 타입을 생략하자.

## 람다의 전달
#### 람다를 변수에 대입하기
```java
public static void main(String[] args) {

    MyFunction add = (int a, int b) -> a + b;
    MyFunction sub = (int a, int b) -> a - b;

    System.out.println("add.apply(1, 2) = " + add.apply(1, 2));
    System.out.println("sub.apply(1, 2) = " + sub.apply(1, 2));

    MyFunction cal = add;
    System.out.println("call(add).apply(1, 2) = " + cal.apply(1, 2));

    cal = sub;
    System.out.println("call(sub).apply(1, 2) = " + cal.apply(1, 2));
}
```
람다도 인터페이스(함수형 인터페이스)를 사용하므로, 람다 인스턴스의 참조값을 변수에 전달할 수 있다.
변수에 참조값을 전달할 수있으므로 다음과 같이 사용할 수 있다.
* 매개변수를 통해 메서드(함수)에 람다를 전달할 수 있다.(람다 인스턴스의 참조값 반환)
* 메서드가 람다를 반환할 수 있다.(람다 인스턴스의 참조값을 반환)

```java
public static void main(String[] args) {
    MyFunction add = (a, b) -> a + b;
    MyFunction sub = (a, b) -> a - b;

    System.out.println("변수를 통해 전달");
    calculate(add);
    calculate(sub);

    System.out.println("람다를 직접 전달");
    calculate((a, b) -> a + b);
    calculate((a, b) -> a - b);
}

static void calculate(MyFunction function) {
    int a = 1;
    int b = 2;

    System.out.println("계산 시작");
    int result = function.apply(a, b);
    System.out.println("계산 결과: " + result);
}
```
#### 람다를 반환하기
```java
public static void main(String[] args) {
    MyFunction add = getOperation("add");
    System.out.println("add.apply(1, 2) = " + add.apply(1, 2));

    MyFunction sub = getOperation("sub");
    System.out.println("sub.apply(1, 2) = " + sub.apply(1, 2));

    MyFunction xxx = getOperation("xxx");
    System.out.println("xxx.apply(1, 2) = " + xxx.apply(1, 2));
}

static MyFunction getOperation(String operation) {
    switch (operation) {
        case "add":
            return (a, b) -> a + b;
        case "sub":
            return (a, b) -> a - b;
        default:
            return (a, b) -> 0;
    }
}
```
* `getOperation` 메서드는 반환 타입이 `MyFunction` 함수형 인터페이스이다. 따라서 람다를 반환할 수 있다.

## 고차 함수
### 고차 함수(Higher-Order Function)
고차 함수는 함수를 값처럼 다루는 함수를 뜻한다.
일반적으로 다음 두 가지 중 하나를 만족하면 고차 함수라 한다.
* 함수를 인자로 받는 함수(메서드)
```java
static void calculate(MyFunction function) {    
    // ...
}
```
* 함수를 반환하는 함수(메서드)
```java
static MyFunction getOperation(String operator) {
    // ...
    return (a, b) -> a + b;
}
```
추상화의 수준(계층, order)이 한 단계 높아진다고해서 High-Order 함수라고 부른다.

# 함수형 인터페이스
## 함수형 인터페이스와 제네릭1
#### 각각 다른 타입 사용
```java
public static void main(String[] args) {
    StringFunction upperCase = s -> s.toUpperCase();
    
    String result1 = upperCase.apply("hello");
    System.out.println("result1 = " + result1);
    
    NumberFunction square = n -> n * n;
    int result2 = square.apply(3);
    System.out.println("result2 = " + result2);
}

@FunctionalInterface
interface StringFunction {
    String apply(String s);
}

@FunctionalInterface
interface NumberFunction {
      int apply(int n);
}
```
`StringFunction`와 `NumberFunction` 둘다 하나의 인자를 입력 받고, 결과를 반환한다.
입력 받는 타입과 반환 타입이 다를 뿐이다.
매개변수나 반환 타입이 다를 때마다 계속 함수형 인터페이스를 만들어야 할까?

#### Object 타입으로 합치기
```java
public static void main(String[] args) {
    ObjectFunction upperCase = s -> ((String) s).toUpperCase();

    String result1 = (String) upperCase.apply("hello");
    System.out.println("result1 = " + result1);

    ObjectFunction square = n -> (Integer) n * (Integer) n;
    int result2 = (Integer) square.apply(3);
    System.out.println("result2 = " + result2);
}

@FunctionalInterface
interface ObjectFunction {
    Object apply(Object s);
}
```
`Object`와 다형성을 활용한 덕분에 중복 코드를 제거하고, 재사용성을 높이게 되었다.
하지만 `Object`를 사용하므로 다운 캐스팅을 해야하고, 결과적으로 안전성 문제가 발생한다.

## 함수형 인터페이스와 제네릭2
```java
public static void main(String[] args) {
    GenericFunction<String, String> upperCase = s -> s.toUpperCase();

    String result1 = upperCase.apply("hello");
    System.out.println("result1 = " + result1);

    GenericFunction<Integer, Integer> square = n -> n * n;
    int result2 = square.apply(3);
    System.out.println("result2 = " + result2);
}

@FunctionalInterface
interface GenericFunction<T, R> {
    R apply(T s);
}
```
정리
* 제네릭을 사용하면 동일한 구조의 함수형 인터페이스를 다양한 타입에 재사용할 수 있다.
* 예제에서는 문자열을 대문자로 변환하기, 문자열의 길이 구하기, 숫자의 제곱 구하기, 짝수 여부 확인하기 등 서로 다른 기능들을 하나의 함수형 인터페이스로 구현했다.
* `T`는 입력타입을, `R`은 반환 타입이며, 실제 사용할 때 구체적인 타입을 지정하면 된다.
* 이렇게 제네릭을 활용하면 타입 안전성을 보장하면서도 유연한 코드를 작성할 수 있다.
* 컴파일 시점에 타입 체크가 이루어지므로 런타임 에러를 방지할 수 있다.
* 제네릭을 사용하지 않았다면 각각의 경우에 대해 별도의 함수형 인터페이스를 만들어야 했을 것이다.
* 이는 코드의 중복을 줄이고 유지보수성을 높이는데 큰 도움이 된다.

## 람다와 타겟 타입
```java
public static void main(String[] args) {
    // 람다 직접 대입: 문제 없음
    FunctionA functionA = i -> "value = " + i;
    FunctionB functionB = i -> "value = " + i;

    // 이미 만들어진 FunctionA 인스턴스를 FunctionB에 대입: 불가능
    // FunctionB targetB = functionA;  // 컴파일 에러!
}

@FunctionalInterface
interface FunctionA {
    String apply(Integer i);
}
@FunctionalInterface
interface FunctionB {
    String apply(Integer i);
}
```
정리
* 람다는 익명 함수로서 특정 타입을 가지지 않고, 대입되는 참조 변수가 어떤 함수형 인터페이스를 가리키느냐에 따라 타입이 결정된다.
* 한편 이미 대입된 변수(`functionA`)는 엄연히 `FunctionA` 타입의 객체가 되었으므로, 이를 `FunctionB` 참조 변수에 그대로 대입할 수는 없다. 두 인터페이스 이름이 다르기 때문에 자바 컴파일러는 다른 타입으로 간주한다.
* 따라서 시그니처가 똑같은 함수형 인터페이스라도, 타입이 다르면 상호 대입이 되지 않는 것이 자바의 타입 시스템 규칙이다.

#### 자바가 기본으로 제공하는 함수형 인터페이스
자바는 이런 문제들을 해결하기 위해 필요한 함수형 인터페이스 대부분을 기본으로 제공한다.
자바가 제공하는 함수형 인터페이스를 사용하면, 비슷한 함수형 인터페이스를 불필요하게 만드는 문제는 물론이고, 함수형 인터페이스의 호환성 문제까지 해결할 수 있다.

#### Function - 자바 기본 제공
```java
package java.util.function;

@FunctionalInterface
public interface Function<T, R> {
    R apply(T t);
}
```

```java
public static void main(String[] args) {
    Function<Integer, String> functionA = i -> "value = " + i;
    System.out.println(functionA.apply(10));
    
    Function<Integer, String> functionB = functionA;
    System.out.println(functionB.apply(20));
}
```

## 기본 함수형 인터페이스
자바가 제공하는 대표적인 함수형 인터페이스
* `Function`: 입력O, 반환O
* `Consumer`: 입력O, 반환X
* `Supplier`: 입력X, 반환O
* `Runnable`: 입력X, 반환X

#### Function
```java
package java.util.function;

@FunctionalInterface
public interface Function<T, R> {
    R apply(T t);
}
```
* 하나의 매개변수를 받고, 결과를 반환하는 함수형 인터페이스이다.
* 입력값(T)를 받아서 다른 타입의 출력값(R)을 반환하는 연산을 표현할 때 사용한다.
* 일반적인 함수(Function)의 개념에 가장 가깝다.
* 

#### Consumer
```java
package java.util.function;

@FunctionalInterface
public interface Consumer<T> { 
    void accept(T t);
}
```
* 입력값(T)만 받고, 결과를 반환하지 않는(void) 연산을 수행하는 함수형 인터페이스이다.
* 입력값(T)을 받아서 처리하지만 결과를 반환하지 않는 연산을 표현할 때 사용한다.
* 입력 받은 데이터를 기반으로 내부적으로 처리하는 경우에 유용하다.

#### Supplier
```java
package java.util.function;

@FunctionalInterface
public interface Supplier<T> {
    T get();
}
```
* 입력을 받지 않고 어떤 데이터를 공급(supply)해주는 함수형 인터페이스이다.
* 객체나 값 생성, 지연 초기화 등에 주로 사용된다.

#### Runnable
```java
package java.lang;

@FunctionalInterface
public interface Runnable { 
    void run();
}
```
* 입력값도 없고 반환값도 없는 함수형 인터페이스이다.
* 주로 멀티스레딩에서 스레드의 작업을 정의할 때 사용한다.
* 입력값도 없고, 반환값도 없는 함수형 인터페이스가 필요할 때 사용한다.

## 특화 함수형 인터페이스
특화 함수형 인터페이스는 의도를 명확하게 만든 조금 특별한 함수형 인터페이스다.
* Predicate: 입력O, 반환 boolean
  * 조건 검사, 필터링 용도
* Operator(UnaryOperator, BinaryOperator): 입력O, 반환O
  * 동일한 타입의 연산 수행, 입력과 같은 타입을 반환하는 연산 용도

#### Predicate
```java
package java.util.function;

@FunctionalInterface
public interface Predicate<T> { 
    boolean test(T t);
}
```
* 입력값(T)을 받아서 true 또는 false로 구분하는 함수형 인터페이스이다.
* 조건 검사, 필터링 등의 용도로 많이 사용된다.

`Function<T, Boolean>`로도 같은 기능을 구현할 수는 있지만, 
목적(조건 검사)과 용도(필터링 등)에 대해 더 분명히 표현하고, 
가독성과 유지보수를 위해 `Predicate<T>` 라는 별도의 함수형 인터페이스가 마련되었다.

#### Operator
Operator는 `UnaryOperator`, `BinaryOperator` 2가지 종류가 제공된다.

#### UnaryOperator(단항 연산자)
```java
package java.util.function;

@FunctionalInterface
public interface UnaryOperator<T> extends Function<T, T> {
    T apply(T t); // 실제 코드가 있지는 않음
}
```
* 단한 연산은 하나의 피연산자에 대해 연산을 수행하는 것을 말한다.
* 입력(피연산자)과 결과(연산 결과)가 동일한 타입인 연산을 수행할 때 사용한다.
* `Function<T, T>`를 상속받는데, 입력과 반환을 모두 같은 `<T>`로 고정한다. 따라서 입력과 반환의 타입이 같아야 한다.

#### BinaryOperator(이항 연산자)
```java
package java.util.function;

@FunctionalInterface
public interface BinaryOperator<T> extends BiFunction<T,T,T> {
    T apply(T t1, T t2); // 실제 코드가 있지는 않음
}
```
* 이항 연산은 두 개의 피연산자에 대해 연산을 수행하는 것을 말한다.
* 같은 타입의 두 입력을 받아, 같은 타입의 결과를 반환할 때 사용한다.
* `BiFunction<T, T, T>`를 상속받는 방식으로 구현되어 있는데, 입력값 2개와 반환을 모두 같은 T로 고정한다. 따라서 `BinaryOperator`는 모든 입력값과 반환 타입이 반드시 같아야 한다.

#### Operator를 제공하는 이유
`Function<T, R>`와 `BiFunction<T, U, R>` 만으로도 사실상 거의 모든 함수형 연산을 구현할 수 있지만,
`UnaryOperator<T>`와 `BinaryOperator<T>`를 별도로 제공하는 이유는 다음과 같다.

1. 의도(목적)의 명시성
   * `UnaryOperator<T>`는 입력과 출력 타입이 동일한 단항 연산을 수행한다는 것을 한 눈에 보여준다.
   * `BinaryOperator<T>`는 같은 타입을 두 개 입력받아 같은 타입을 결과로 반환하는 이항 연산을 수행한다는 것을 명확히 드러낸다.
2. 가독성과 유지보수성
   * 제네릭을 적는 코드의 양도 하나로 줄일 수 있다.
   * 여러 사람이 협업하는 프로젝트에서 이런 명시성 코드가 가독성과 유지보수성에 큰 도움이 된다.

## 기타 함수형 인터페이스
매개변수가 2개 이상 필요한 경우 `Bixxx` 시리즈를 사용하면 된다.
```java
public static void main(String[] args) {
    BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
    System.out.println("add = " + add.apply(5, 10));

    BiConsumer<String, Integer> repeat = (a, b) -> {
        for (int i = 0; i < b; i++) {
            System.out.print(a);
        }
        System.out.println();
    };
    repeat.accept("hello", 5);

    BiPredicate<Integer, Integer> isGreater = (a, b) -> a > b;
    System.out.println("isGreater = " + isGreater.test(10, 5));
}
```

#### 입력값이 3개라면?
보통 함수형 인터페이스를 사용할 때 3개 이상의 매개변수는 잘 사용하지 않지만 필요한 경우 직접 만들어 사용하면 된다.
```java
public static void main(String[] args) {
    TriFunction<Integer, Integer, Integer, Integer> triFunction = (a, b, c) -> a + b + c;
    System.out.println("triFunction.apply(1, 2, 3) = " + triFunction.apply(1, 2, 3));
}

@FunctionalInterface
interface TriFunction<A, B, C, R> {
    R apply(A a, B b, C c);
}
```

#### 기본형 지원 함수형 인터페이스
```java
package java.util.function;

@FunctionalInterface
public interface IntFunction<R> {
    R apply(int value);
}
```
기본형 지원 함수형 인터페이스가 존재하는 이유
* 오토박싱/언박싱으로 인한 성능 비용을 줄이기 위해
* 자바 제네릭의 한계(제네릭은 primitive 타입을 직접 다를 수 없음)를 극복하기 위해

# 람다 활용
## 필터 만들기1
```java
public static void main(String[] args) {
    List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    // 짝수만 거르기
    List<Integer> evenNumbers = filterEvenNumber(numbers);
    System.out.println("evenNumbers = " + evenNumbers);

    // 홀수만 거르기
    List<Integer> oddNumbers = filterOddNumber(numbers);
    System.out.println("oddNumbers = " + oddNumbers);
}

private static List<Integer> filterEvenNumber(List<Integer> numbers) {
    List<Integer> filteredNumber = new ArrayList<>();
    for (Integer number : numbers) {
        if (number % 2 == 0) {
            filteredNumber.add(number);
        }
    }
    return filteredNumber;
}

private static List<Integer> filterOddNumber(List<Integer> numbers) {
    List<Integer> filteredNumber = new ArrayList<>();
    for (Integer number : numbers) {
        if (number % 2 == 1) {
            filteredNumber.add(number);
        }
    }
    return filteredNumber;
}
```

```java
public static void main(String[] args) {
    List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    // 짝수만 거르기
    List<Integer> evenNumbers = filter(numbers, x -> x % 2 == 0);
    System.out.println("evenNumbers = " + evenNumbers);

    // 홀수만 거르기
    List<Integer> oddNumbers = filter(numbers, x -> x % 2 == 1);
    System.out.println("oddNumbers = " + oddNumbers);

}

private static List<Integer> filter(List<Integer> numbers, Predicate<Integer> predicate) {
    List<Integer> filteredNumber = new ArrayList<>();
    for (Integer number : numbers) {
        if (predicate.test(number)) {
            filteredNumber.add(number);
        }
    }
    return filteredNumber;
}
```

## 필터 만들기2
```java
public static <T>List<T> filter(List<T> list, Predicate<T> predicate) {
    List<T> filtered = new ArrayList<>();
    for (T number : list) {
        if (predicate.test(number)) {
            filtered.add(number);
        }
    }
    return filtered;
}
```

## 맵 만들기1
```java
public static void main(String[] args) {

    List<String> list = List.of("1", "12", "123", "1234");

    List<Integer> numbers = mapStringToInteger(list);
    System.out.println("numbers = " + numbers);

    List<Integer> lengths = mapStringToLength(list);
    System.out.println("lengths = " + lengths);
}

private static List<Integer> mapStringToInteger(List<String> list) {
    List<Integer> numbers = new ArrayList<>();
    for (String element : list) {
        numbers.add(Integer.valueOf(element));
    }
    return numbers;
}

private static List<Integer> mapStringToLength(List<String> list) {
    List<Integer> numbers = new ArrayList<>();
    for (String element : list) {
        numbers.add(element.length());
    }
    return numbers;
}
```

```java
public static void main(String[] args) {

  List<String> list = List.of("1", "12", "123", "1234");

  List<Integer> toNumber = map(list, s -> Integer.valueOf(s));
  System.out.println("numbers = " + toNumber);

  List<Integer> toLength = map(list, s -> s.length());
  System.out.println("lengths = " + toLength);
}

private static List<Integer> map(List<String> list, Function<String, Integer> mapper) {
    List<Integer> numbers = new ArrayList<>();
    for (String element : list) {
        numbers.add(mapper.apply(element));
    }
    return numbers;
}
```

## 맵 제네릭 도입
```java
public class GenericMapper {

    public static <T, R> List<R> map(List<T> list, Function<T, R> mapper) {
        ArrayList<R> numbers = new ArrayList<>();
        for (T element : list) {
            numbers.add(mapper.apply(element));
        }
        return numbers;
    }
}
```
## 필터와 맵 활용1
#### 필터와 맵 활용 - 문제1
리스트에 있는 값 중에 짝수만 남기고, 남은 짝수 값의 2배를 반환해라.
```java
public class Ex1_Number {

    public static void main(String[] args) {
        // 짝수만 남기고, 남은 값의 2배를 반환
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> directResult = direct(numbers);
        System.out.println("directResult = " + directResult);
        List<Integer> lambdaResult = lambda(numbers);
        System.out.println("lambdaResult = " + lambdaResult);
    }

    static List<Integer> direct(List<Integer> numbers) {
        List<Integer> result = new ArrayList<>();

        for (Integer number : numbers) {
            if (number % 2 == 0) {
                result.add(number * 2);
            }
        }
        
        return result;
    }

    static List<Integer> lambda(List<Integer> numbers) {
        List<Integer> filtered = GenericFilter.filter(numbers, n -> n % 2 == 0);
        return GenericMapper.map(filtered, n -> n * 2);
    }
}
```

`direct()`와 `lambda()`는 서로 다른 프로그래밍 스타일을 보여준다.   
`direct()`는 프로그램을 어떻게 수행해야 하는지 수행 절차를 명시한다.   
`lambda()`는 무엇을 수행해야 하는지 원하는 결과에 초점을 맞춘다.

#### 명령형 vs 선언적 프로그래밍
명령형 프로그래밍(Imperative Programming)
* 정의: 프로그래밍이 어떻게(How) 수행되어야 하는지, 즉 수행 절차를 명시하는 방식이다.
* 특징:
  * 단계별 실행: 프로그램의 각 단계를 명확하게 지정하고 순서대로 실행한다.
  * 상태 변화: 프로그램의 상태(변수 값 등)가 각 단계별로 어떻게 변화하는지 명시한다.
  * 낮은 추상화: 내부 구현을 직접 제어해야 하므로 추상화 수준이 낮다.
  * 예시: 전통적인 for 루프, while 루프 등을 명시적으로 사용하는 방식
  * 장점: 시스템의 상태와 흐름을 세밀하게 제어할 수 있다.

선언적 프로그래밍(Declarative Programming)
* 정의: 프로그램이 무엇(What)을 수행해야 하는지, 즉 원하는 결과를 명시하는 방식이다.
* 특징:
  * 문제 해결에 집중: 어떻게(How) 문제를 해결할지 보다 무엇을 원하는지에 초점을 맞춘다.
  * 코드 간결성: 간결하고 읽기 쉬운 코드를 작성할 수 있다.
  * 예시: `filter`, `map` 등 람다의 고차 함수를 활용
* 장점: 코드가 간결하고, 의도가 명확하며, 유지보수가 쉬운 경우가 많다.

## 필터와 맵 활용2
#### 필터와 맵 활용 - 문제2
```java
public class Ex2_Student {

    public static void main(String[] args) {
        // 점수가 80점 이상인 학생의 이름을 추출해라.
        List<Student> students = List.of(
                new Student("Apple", 100),
                new Student("Banana", 80),
                new Student("Berry", 50),
                new Student("Tomato", 40)
        );
        List<String> directResult = direct(students);
        System.out.println("directResult = " + directResult);
        List<String> lambdaResult = lambda(students);
        System.out.println("lambdaResult = " + lambdaResult);
    }

    private static List<String> direct(List<Student> students) {
        List<String> result = new ArrayList<>();
        for (Student student : students) {
            if (student.getScore() >= 80) {
                result.add(student.getName());
            }
        }
        return result;
    }

    private static List<String> lambda(List<Student> students) {
        List<Student> filtered = GenericFilter.filter(students, student -> student.getScore() >= 80);
        return GenericMapper.map(filtered, student -> student.getName());
    }
}
```

## 스트림 만들기1
#### 스트림1
필터와 맵을 사용할 때 데이터들이 흘러가면서 필터되고, 매핑된다.
이렇듯 데이터가 흘러가면서 필터도 되고 매핑도 되는 클래스의 이름을 스트림(Stream)이라고 짓자
```java
public class MyStreamV1 {

    private List<Integer> internalList;

    public MyStreamV1(List<Integer> internalList) {
        this.internalList = internalList;
    }

    public MyStreamV1 filter(Predicate<Integer> predicate) {
        List<Integer> filtered = new ArrayList<>();
        for (Integer number : internalList) {
            if (predicate.test(number)) {
                filtered.add(number);
            }
        }
        return new MyStreamV1(filtered);
    }

    public MyStreamV1 map(Function<Integer, Integer> mapper) {
        List<Integer> mapped = new ArrayList<>();
        for (Integer number : internalList) {
            mapped.add(mapper.apply(number));
        }
        return new MyStreamV1(mapped);
    }

    public List<Integer> toList() {
        return internalList;
    }
}
```
```java
public class MyStreamMainV1 {

    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        List<Integer> result1 = returnValue(numbers);
        System.out.println("result1 = " + result1);

        List<Integer> result2 = methodChain(numbers);
        System.out.println("result2 = " + result2);
    }

    private static List<Integer> methodChain(List<Integer> numbers) {
        MyStreamV1 stream = new MyStreamV1(numbers);
        MyStreamV1 result = stream.filter(n -> n % 2 == 0).map(n -> n * 2);
        return result.toList();
    }

    private static List<Integer> returnValue(List<Integer> numbers) {
        MyStreamV1 stream = new MyStreamV1(numbers);
        MyStreamV1 filteredStream = stream.filter(n -> n % 2 == 0);
        System.out.println("filteredStream = " + filteredStream.toList());

        MyStreamV1 mappedStream = filteredStream.map(n -> n * 2);
        System.out.println("mappedStream = " + mappedStream.toList());
        return mappedStream.toList();
    }
}
```

## 스트림 만들기2
```java
public class MyStreamV2 {

    private List<Integer> internalList;

    private MyStreamV2(List<Integer> internalList) {
        this.internalList = internalList;
    }

    public static MyStreamV2 of(List<Integer> internalList) {
        return new MyStreamV2(internalList);
    }

    public MyStreamV2 filter(Predicate<Integer> predicate) {
        List<Integer> filtered = new ArrayList<>();
        for (Integer number : internalList) {
            if (predicate.test(number)) {
                filtered.add(number);
            }
        }
        return new MyStreamV2(filtered);
    }

    public MyStreamV2 map(Function<Integer, Integer> mapper) {
        List<Integer> mapped = new ArrayList<>();
        for (Integer number : internalList) {
            mapped.add(mapper.apply(number));
        }
        return new MyStreamV2(mapped);
    }

    public List<Integer> toList() {
        return internalList;
    }
}
```
```java
public class MyStreamV2Main {

    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        List<Integer> result = MyStreamV2.of(numbers)
                .filter(n -> n % 2 == 0)
                .map(n -> n * 2).toList();

        System.out.println("result = " + result);
    }

}
```
#### 정적 팩토리 메서드 - static factory method
정적 팩토리 메서드는 객체 생성을 담당하는 static 메서드로, 생성자(constructor) 대신 인스턴스를 생성하고 반환하는 역할을 한다.
즉, 일반적인 생성자(Constructor) 대신에 메서드 클래스의 인스턴스를 생성하고 초기화하는 로직을 캡슐화 하여 정적(static) 메서드이다.

## 스트림 만들기3
```java
public class MyStreamV3<T> {

    private List<T> internalList;

    private MyStreamV3(List<T> internalList) {
        this.internalList = internalList;
    }

    public static <T> MyStreamV3<T> of(List<T> internalList) {
        return new MyStreamV3<>(internalList);
    }

    public MyStreamV3<T> filter(Predicate<T> predicate) {
        List<T> filtered = new ArrayList<>();
        for (T number : internalList) {
            if (predicate.test(number)) {
                filtered.add(number);
            }
        }
        return MyStreamV3.of(filtered);
    }

    public <R> MyStreamV3<R> map(Function<T, R> mapper) {
        List<R> mapped = new ArrayList<>();
        for (T number : internalList) {
            mapped.add(mapper.apply(number));
        }
        return MyStreamV3.of(mapped);
    }

    public List<T> toList() {
        return internalList;
    }
}
```
```java
public class MyStreamV3Main {

    public static void main(String[] args) {
        List<Student> students = List.of(
                new Student("Apple", 100),
                new Student("Banana", 80),
                new Student("Berry", 50),
                new Student("Tomato", 40)
        );

        // 점수가 80점 이상인 학생의 이름을 추출
        List<String> result1 = ex1(students);
        System.out.println("result1 = " + result1);

        // 점수가 80점 이상이면서, 이름이 5글자인 학생의 이름을 대문자로 추출
        List<String> result2 = ex2(students);
        System.out.println("result2 = " + result2);
    }

    private static List<String> ex1(List<Student> students) {
        return MyStreamV3.of(students)
                .filter(s -> s.getScore() >= 80)
                .map(s -> s.getName())
                .toList();
    }

    private static List<String> ex2(List<Student> students) {
        return MyStreamV3.of(students)
                .filter(s -> s.getScore() >= 80)
                .filter(s -> s.getName().length() == 5)
                .map(s -> s.getName().toUpperCase())
                .toList();
    }
}
```

## 스트림 만들기 4
```java
public class MyStreamV3<T> {

    private List<T> internalList;

    private MyStreamV3(List<T> internalList) {
        this.internalList = internalList;
    }

    public static <T> MyStreamV3<T> of(List<T> internalList) {
        return new MyStreamV3<>(internalList);
    }

    public MyStreamV3<T> filter(Predicate<T> predicate) {
        List<T> filtered = new ArrayList<>();
        for (T number : internalList) {
            if (predicate.test(number)) {
                filtered.add(number);
            }
        }
        return MyStreamV3.of(filtered);
    }

    public <R> MyStreamV3<R> map(Function<T, R> mapper) {
        List<R> mapped = new ArrayList<>();
        for (T number : internalList) {
            mapped.add(mapper.apply(number));
        }
        return MyStreamV3.of(mapped);
    }

    public List<T> toList() {
        return internalList;
    }

    public void forEach(Consumer<T> consumer) {
        for (T number : internalList) {
            consumer.accept(number);
        }
    }
}
```
```java
public static void main(String[] args) {
    List<Student> students = List.of(
            new Student("Apple", 100),
            new Student("Banana", 80),
            new Student("Berry", 50),
            new Student("Tomato", 40)
    );

    // 내부 반복
    MyStreamV3.of(students)
            .filter(s -> s.getScore() >= 80)
            .map(s -> s.getName())
            .forEach(name -> System.out.println(name));


    // 외부 반복
    for (Student student : students) {
        System.out.println(student.getName());
    }

}
```
#### 내부 반복 vs 외부 반복
스트림을 사용하기 전에 일반적인 반복 방식은 `for`, `while` 같은 반복문을 직접 사용해서 데이터를 순회하는 외부 반복 방식이었다.
스트림에서 제공하는 `forEach` 메서드로 데이터를 처리하는 방식을 내부 반복이라고 부른다.
반복 제어를 스트림이 대신 수행하므로, 사용자는 반복 로직을 신경 쓸 필요가 없다.
코드가 훨씬 간결해자며, 선언형 프로그래밍 스타일을 적용할 수 있다.


# 람다 vs 익명 클래스
자바에서 익명 클래스와 람다 표현식은 모두 간단하게 기능을 구현하거나, 일회성으로 사용할 객체를 만들 때 유용하지만, 그 사용 방식과 의도에는 차이가 있다.

1. 문법차이
  * 익명 클래스
    * 익명 클래스는 클래스를 선언하고 즉시 인스턴스를 생성하는 방식이다.
    * 반드시 `new 인터페이스명() {...}` 형태로 작성해야 하며, 메서드를 오버라이드해서 구현한다.
    * 익명 클래스도 하나의 클래스이다.
```java
Button button = new Button();
button.setOnClickListener(new OnClickListener() {
   @Override
   public void onClick(View v) {
       System.out.println("버튼 클릭");
  }
});
```
  * 람다 표현식
    * 람다 표현식은 함수를 간결하게 표현할 수 있는 방식이다.
    * 함수형 인터페이스(메서드 하나인 인터페이스)를 간단히 구현할 때 주로 사용한다.
    * 람다는 `->` 연산자를 사용하여 표현하며, 매개변수와 실행할 내용을 간결하게 작성할 수 있다.
    * 람다도 인스턴스가 생성된다.
```java
Button button = new Button();
button.setOnClickListener(v -> System.out.println("버튼 클릭"));
```
2. 코드 간결함
* 익명 클래스는 문법적으로 더 복잡하고 장황하다.
* 람다 표현식은 간결하며, 불필요한 코드를 최소화한다.

3. 상속 관계
* 익명 클래스는 일반적인 클래스처럼 다양한 인터페이스와 클래스를 구현하거나 상속할 수 있다. 즉 여러 메서드를 가진 인터페이스를 구현할 때도 사용할 수 있다.
* 람다 표현식은 메서드를 딱 하나만 가지는 함수형 인터페이스만을 구현할 수 있다.

4. 호환성
* 익명 클래스는 자바의 오래된 버전에서도 사용할 수 있다.
* 람다 표현식은 자바 8부터 도입되었기 때문에 그 이전 버전에서는 사용할 수 없다.

5. this 키워드의 의미
* 익명 클래스 내부에서 `this`는 익명 클래스 자신을 가리킨다. 외부 클래스와 별도의 컨텍스트를 가진다.
* 람다 표현식은 `this`는 람다를 선언한 클래스의 인스턴스를 가리킨다. 즉, 람다 표현식은 별도의 컨텍스트를 가지는 것이 아니라, 람다를 선언한 클래스의 컨텍스트를 유지한다.

6. 캡처링
* 익명 클래스
  * 익명 클래스는 외부 변수에 접근할 수있지만, 지역 변수는 반드시 `final` 혹은 사실상 final인 변수만 캡처할 수 있다.
* 람다 표현식
  * 람다도 익명 클래스와 같이 캡처링을 지원한다. 지역 변수는 반드시 `final` 혹은 사실상 final인 변수만 캡처할 수있다.

7. 생성 방식
* 익명 클래스
  * 익명 클래스는 새로운 클래스를 정의하여 객체를 생성하는 방식이다. 즉, 컴파일 시 새로운 내부 클래스로 변환된다.
  * 이 방싱근 클래스가 메모리 상에서 별도로 관리되므로 메모리 상에 약간의 추가 오버헤드가 발생한다.
* 람다
  * 내부적으로 `invokeDynamic`이라는 메커니즘을 사용하여 컴파일 타임에 실제 클래스 파일을 생성하지 않고, 런타임 시점에 동적으로 필요한 코드를 처리한다.
  * 람다는 익명 클래스보다 메모리 관리가 더 효율적이며, 생성된 클래스 파일이 없으므로 클래스 파일 관리의 복잡성이 줄어든다.

8. 상태 관리
* 익명 클래스
  * 익명 클래스는 인스턴스 내부에서 상태(필드, 멤버 변수)를 가질 수 있다. 예를 들어 익명 클래스 내부에 멤버 변수를 선언하고 해당 변수의 값을 변경하거나 상태를 관리할 수 있다.
  * 이처럼 상태를 필요로 하는 경우, 익명 클래스가 유리하다.
* 람다
  * 클래스는 그 내부에 상태(필드, 멤버 변수)와 기능(메서드)을 가진다. 반면 함수는 그 내부에 상태(필드)를 가지지 않고 기능만 제공한다.
  * 함수인 람다는 기본적으로 필드(멤버 변수)가 없으므로 스스로 상태를 유지하지 않는다.

9. 익명 클래스와 람다의 용도 구분
* 익명 클래스
  * 상태를 유지하거나 다중 메서드를 구현할 필요가 있는 경우
  * 기존 클래스 또는 인터페이스를 상송하거나 구현할 때
  * 복잡한 인터페이스 구현이 필요할 때
* 람다
  * 상태를 유지할 필요가 없고, 간결함이 중요한 경우
  * 더 나은 성능(이 부분은 미미함)과 간결한 코드가 필요한 경우

# 메서드 참조
## 메서드 참조가 필요한 이유
```java
public static void main(String[] args) {
    BinaryOperator<Integer> add1 = (x ,y) -> x + y;
    BinaryOperator<Integer> add2 = (x ,y) -> x + y;

    Integer result1 = add1.apply(1, 2);
    System.out.println("result1 = " + result1);

    Integer result2 = add2.apply(1, 2);
    System.out.println("result2 = " + result2);
}
```
```java
public static void main(String[] args) {
    BinaryOperator<Integer> add1 = (x ,y) -> add(x, y);
    BinaryOperator<Integer> add2 = (x ,y) -> add(x, y);

    Integer result1 = add1.apply(1, 2);
    System.out.println("result1 = " + result1);

    Integer result2 = add2.apply(1, 2);
    System.out.println("result2 = " + result2);
}

private static int add(int x, int y) {
    return x + y;
}
```
```java
public static void main(String[] args) {
    BinaryOperator<Integer> add1 = MethodRefStartV3::add;
    BinaryOperator<Integer> add2 = MethodRefStartV3::add;

    Integer result1 = add1.apply(1, 2);
    System.out.println("result1 = " + result1);

    Integer result2 = add2.apply(1, 2);
    System.out.println("result2 = " + result2);
}

private static int add(int x, int y) {
    return x + y;
}
```
#### 메서드 참조의 장점
* 메서드 참조를 사용하면 코드가 더욱 간결해지고, 가독성이 향상된다.
* 더 이상 매개변수를 명시적으로 작성할 필요가 없다.
  * 컴파일러가 자동으로 매개변수를 매칭한다.
* 별도의 로직 분리와 함께 재사용성 역시 높아진다.

## 메서드 참조 - 시작
#### 메서드 참조의 4가지 유형
1. 정적 메소드 참조
2. 특정 객체의 인스턴스 메서드 참조
3. 생성자 참조
4. 임의 객체의 인스턴스 메서드 참조

```java
public static void main(String[] args) {
    // 1. 정적 메서드 참조
    Supplier<String> staticMethod1 = () -> Person.greeting();
    Supplier<String> staticMethod2 = Person::greeting;  // 클래스::정적메서드
    System.out.println("staticMethod1.get() = " + staticMethod1.get());
    System.out.println("staticMethod2.get() = " + staticMethod2.get());

    // 2. 특정 객체의 인스턴스 참조
    Person person = new Person("Kim");
    Supplier<String> instanceMethod1 = () -> person.introduce();
    Supplier<String> instanceMethod2 = person::introduce;
    System.out.println("instanceMethod1 = " + instanceMethod1.get());
    System.out.println("instanceMethod2 = " + instanceMethod2.get());

    // 3. 생성자 참조
    Supplier<Person> newPerson1 = () -> new Person();
    Supplier<Person> newPerson2 = Person::new;
    System.out.println("newPerson1 = " + newPerson1.get());
    System.out.println("newPerson2 = " + newPerson2.get());
}
```

## 메서드 참조 - 매개변수
```java
public static void main(String[] args) {
    // 1. 정적 메서드 참조
    Function<String, String> staticMethod1 = name -> Person.greetingWithName(name);
    Function<String, String> staticMethod2 = Person::greetingWithName;
    System.out.println("staticMethod1.get() = " + staticMethod1.apply("kim"));
    System.out.println("staticMethod2.get() = " + staticMethod2.apply("kim"));

    // 2. 특정 객체의 인스턴스 참조
    Person person = new Person("Kim");
    Function<Integer, String> instanceMethod1 = n -> person.introduceWithName(n);
    Function<Integer, String> instanceMethod2 = person::introduceWithName;
    System.out.println("instanceMethod1 = " + instanceMethod1.apply(1));
    System.out.println("instanceMethod2 = " + instanceMethod2.apply(1));

    // 3. 생성자 참조
    Function<String, Person> newPerson1 = name -> new Person(name);
    Function<String, Person> newPerson2 = Person::new;
    System.out.println("newPerson1 = " + newPerson1.apply("kim"));
    System.out.println("newPerson2 = " + newPerson2.apply("kim"));
}
```

## 메서드 참조 - 임의 객체의 인스턴스 메서드 참조
```java
public static void main(String[] args) {
    // 4. 임의 객체의 인스턴스 메서드 참조(특정 타입의)
    Person person1 = new Person("Kim");
    Person person2 = new Person("Park");
    Person person3 = new Person("Lee");

    // 람다
    Function<Person, String> fun1 = (Person person) -> person.introduce();
    System.out.println("fun1 = " + fun1.apply(person1));
    System.out.println("fun2 = " + fun1.apply(person2));
    System.out.println("fun3 = " + fun1.apply(person3));

    // 메서드 참조, 타입의 첫 번째 매개변수가 됨
    // 그리고 첫 번째 매개변수의 메서드를 호출, 나머지는 순서대로 매개변수에 전달
    Function<Person, String> fun2 = Person::introduce; // 타입::인스턴스 메서드
    System.out.println("person1.introduce = " + fun2.apply(person1));
    System.out.println("person2.introduce = " + fun2.apply(person2));
    System.out.println("person3.introduce = " + fun2.apply(person3));

}
```
* 특정 객체의 인스턴스 메서드 참조: `객체명::인스턴스메서드`(person::introduce)
* 임의 객체의 인스턴스 메서드 참조: `클래스명::인스턴스메서드`(Person::introduce)

## 메서드 참조 - 활용1
```java
public class MethodRefEx4 {

    public static void main(String[] args) {
        List<Person> personList = List.of(
                new Person("Kim"),
                new Person("Park"),
                new Person("Lee")
        );

        List<String> result1 = mapPersonToString(personList, (Person p) -> p.introduce());
        System.out.println("result1 = " + result1);

        List<String> result2 = mapPersonToString(personList, Person::introduce);
        System.out.println("result2 = " + result2);

        List<String> upperResult1 = mapStringToString(result1, (String s) -> s.toUpperCase());
        System.out.println("upperResult1 = " + upperResult1);

        List<String> upperResult2 = mapStringToString(result1, String::toUpperCase);
        System.out.println("upperResult2 = " + upperResult2);


    }

    private static List<String> mapPersonToString(List<Person> psersonList, Function<Person, String> function) {
        List<String> result = new ArrayList<>();
        for (Person person : psersonList) {
            result.add(function.apply(person));
        }
        return result;
    }

    private static List<String> mapStringToString(List<String> strings, Function<String, String> function) {
        ArrayList<String> result = new ArrayList<>();
        for (String string : strings) {
            result.add(function.apply(string));
        }
        return result;
    }
}
```
람다 대신에 메서드 참조를 사용한 덕분에 코드가 더 간결해지고, 의도가 더 명확하게 드러나는 것을 확인할 수 있다.

## 메서드 참조 - 활용2
```java
public class MethodRefEx5 {

    public static void main(String[] args) {
        List<Person> personList = List.of(
                new Person("Kim"),
                new Person("Park"),
                new Person("Lee")
        );

        List<String> result1 = MyStreamV3.of(personList)
                .map(person -> person.introduce())
                .map(str -> str.toUpperCase())
                .toList();
        System.out.println("result1 = " + result1);

        List<String> result2 = MyStreamV3.of(personList)
                .map(Person::introduce)
                .map(String::toUpperCase)
                .toList();
        System.out.println("result2 = " + result2);
    }
}
```
메서드 참조가 더 짧고 명확하게 표현될 수 있다. 이런 방식은 가독성을 높이는 장점이 있다.

## 메서드 참조 - 활용3
```java
public class MethodRefEx6 {

    public static void main(String[] args) {
        Person person = new Person("Kim");

        // 람다
        BiFunction<Person, Integer, String> function1 = (Person p, Integer n) -> p.introduceWithName(n);
        System.out.println("person.introduceWithNumber = " + function1.apply(person, 1));

        // 메서드 참조, 타입이 첫 번째 매개변수, 첫 번째 매개변수의 메서드를 호출
        BiFunction<Person, Integer, String> function2 = Person::introduceWithName;
        System.out.println("person.introduceWithNumber = " + function2.apply(person, 1));
    }
}
```

# 스트림 API - 기본
## 스트림 API 시작
```java
public static void main(String[] args) {
    List<String> names = List.of("Apple", "Banana", "Berry", "Tomato");
    
    // 'B'로 시작하는 이름만 필터 후 대문자로 바꿔서 리스트 수집
    List<String> upperNames1 = names.stream()
            .filter(name -> name.startsWith("B"))
            .map(s -> s.toUpperCase())
            .toList();
    System.out.println(upperNames1);
    
    
    names.stream()
            .filter(name -> name.startsWith("B"))
            .map(String::toUpperCase)
            .forEach(System.out::println);
    
    List<String> upperNames2 = names.stream()
            .filter(name -> name.startsWith("B"))
            .map(String::toUpperCase)
            .toList();
    System.out.println(upperNames2);
}
```
#### 스트림 생성
```java
List<String> names = List.of("Apple", "Banana", "Berry", "Tomato");
Stream<String> stream = names.stream();
```
* `List`의 `stream()` 메서드를 사용하면 자바가 제공하는 스트림을 생성할 수 있다.

#### 중간 연산(Intermediate Operations) - `filter`, `map`
```java
 .filter(name -> name.startsWith("B"))
 .map(s -> s.toUpperCase())
```

#### 최종 연산(Terminal Operation) - `toList()`
```java
List<String> upperNames2 = names.stream()
            .filter(name -> name.startsWith("B"))
            .map(String::toUpperCase)
            .toList();
```
* `toList()`는 최종연산이다. 중간 연산에서 정의한 연산을 기반으로 최종 결과를 `List`로 만들어 반환한다.

## 스트림 API란?
* 스트림(Stream)은 자바 8부터 추가된 기능으로, 데이터의 흐름을 추상회해서 다루는 도구이다.
* 컬렉션(Collection) 또는 배열 등의 요소들을 연산 파이프라인을 통해 연속적인 형태로 처리할 수 있게 해준다.

#### 스트림의 특징
1. 데이터 소스를 변경하지 않음(Immutable)
   * 스트림에서 제공하는 연산들은 원본 객체(컬렉션)을 변경하지 않고 결과만 새로 생성한다.
2. 일회성(1회 소비)
   * 한 번 사용(소비)된 스트림은 다시 사용할 수 없으며, 필요하다면 새로 스트림을 생성해야 한다.
3. 파이프라인 구성
   * 중간 연산들이 이어지다가, 최종연산을 만나면 연산이 수행되고 종료된다.
4. 지연 연산
   * 중간 연산은 필요할 때까지 실제로 동작하지 않고, 최종 연산이 실행될 때 한 번에 처리된다.
5. 병렬 처리 용이
   * 스트림으로부터 병렬 스트림을 쉽게 만들 수가 있어서, 멀티코어 환경에서 병렬 연산을 비교적 단순한 코드를 작성할 수 있다.

## 파이프라인 구성
```java
public class LazyEvalMain {

    public static void main(String[] args) {
        List<Integer> data = List.of(1, 2, 3, 4, 5, 6);
        
        ex1(data);
        ex2(data);
    }

    private static void ex1(List<Integer> data) {
        System.out.println("== MyStreamV3 시작 ==");
        List<Integer> result = MyStreamV3.of(data)
                .filter(n -> {
                    boolean isEven = n % 2 == 0;
                    System.out.println("fitler() 실행: " + n + "(" + isEven + ")");
                    return isEven;
                })
                .map(n -> {
                    int mapped = n * 10;
                    System.out.println("map() 실행: " + n + " -> " + mapped);
                    return mapped;
                })
                .toList();
        System.out.println(result);
        System.out.println("== MyStreamV3 종료 ==");
    }

    private static void ex2(List<Integer> data) {
        System.out.println("== Stream API 시작 ==");
        List<Integer> result = data.stream()
                .filter(n -> {
                    boolean isEven = n % 2 == 0;
                    System.out.println("fitler() 실행: " + n + "(" + isEven + ")");
                    return isEven;
                })
                .map(n -> {
                    int mapped = n * 10;
                    System.out.println("map() 실행: " + n + " -> " + mapped);
                    return mapped;
                })
                .toList();
        System.out.println(result);
        System.out.println("== Stream API 종료 ==");
    }
}
```
```
== MyStreamV3 시작 ==
fitler() 실행: 1(false)
fitler() 실행: 2(true)
fitler() 실행: 3(false)
fitler() 실행: 4(true)
fitler() 실행: 5(false)
fitler() 실행: 6(true)
map() 실행: 2 -> 20
map() 실행: 4 -> 40
map() 실행: 6 -> 60
[20, 40, 60]
== MyStreamV3 종료 ==
== Stream API 시작 ==
fitler() 실행: 1(false)
fitler() 실행: 2(true)
map() 실행: 2 -> 20
fitler() 실행: 3(false)
fitler() 실행: 4(true)
map() 실행: 4 -> 40
fitler() 실행: 5(false)
fitler() 실행: 6(true)
map() 실행: 6 -> 60
[20, 40, 60]
== Stream API 종료 ==
```
#### 일괄 처리 vs 파이프라인
* 일괄 처리
  * 공정(중간 연산)을 단계별로 쪼개서 데이터 전체를 한 번에 처리하고, 결과를 저장해두었다가 다음 공정을 또 한 번에 수행한다.
* 파이프라인 
  * 한 단계가 끝나면 바로 다음 단계로 넘기면서 연결되어 있는 형태이다.

#### 정리
* 자바 스트림은 중간 단계에서 데이터를 모아서 한 방에 처리하지 않고, 한 요소가 중간 연산을 통과하면 곧 바로 다음 중간 연산으로 이어지는 파이프라인 형태를 가진다.

## 지연 연산
최종 연산을 수행해야 작동한다.
자바 스트림은 `toList()`와 같은 최종 연산을 수행할 때만 작동한다.

```java
public class LazyEvalMain2 {

    public static void main(String[] args) {
        List<Integer> data = List.of(1, 2, 3, 4, 5, 6);
        
        ex1(data);
        ex2(data);
    }

    private static void ex1(List<Integer> data) {
        System.out.println("== MyStreamV3 시작 ==");
        MyStreamV3.of(data)
                .filter(n -> {
                    boolean isEven = n % 2 == 0;
                    System.out.println("fitler() 실행: " + n + "(" + isEven + ")");
                    return isEven;
                })
                .map(n -> {
                    int mapped = n * 10;
                    System.out.println("map() 실행: " + n + " -> " + mapped);
                    return mapped;
                });
        System.out.println("== MyStreamV3 종료 ==");
    }

    private static void ex2(List<Integer> data) {
        System.out.println("== Stream API 시작 ==");
        data.stream()
                .filter(n -> {
                    boolean isEven = n % 2 == 0;
                    System.out.println("fitler() 실행: " + n + "(" + isEven + ")");
                    return isEven;
                })
                .map(n -> {
                    int mapped = n * 10;
                    System.out.println("map() 실행: " + n + " -> " + mapped);
                    return mapped;
                });
        System.out.println("== Stream API 종료 ==");
    }
}
```
```
== MyStreamV3 시작 ==
fitler() 실행: 1(false)
fitler() 실행: 2(true)
fitler() 실행: 3(false)
fitler() 실행: 4(true)
fitler() 실행: 5(false)
fitler() 실행: 6(true)
map() 실행: 2 -> 20
map() 실행: 4 -> 40
map() 실행: 6 -> 60
== MyStreamV3 종료 ==
== Stream API 시작 ==
== Stream API 종료 ==
```
* 스트림 API의 지연 연산을 가장 극명하게 보여주는 예시이다.
* 중간 연산자들은 파이프라인 설정을 해놓기만하고, 정작 실제 연산은 최종 연산이 호출되기 전까지 전혀 진행되지 않는다.

## 지연 연산과 최적화
```java
public class LazyEvalMain3 {

    public static void main(String[] args) {
        List<Integer> data = List.of(1, 2, 3, 4, 5, 6);
        
        ex1(data);
        ex2(data);
    }

    private static void ex1(List<Integer> data) {
        System.out.println("== MyStreamV3 시작 ==");
        Integer result = MyStreamV3.of(data)
                .filter(n -> {
                    boolean isEven = n % 2 == 0;
                    System.out.println("fitler() 실행: " + n + "(" + isEven + ")");
                    return isEven;
                })
                .map(n -> {
                    int mapped = n * 10;
                    System.out.println("map() 실행: " + n + " -> " + mapped);
                    return mapped;
                })
                .getFirst();
        System.out.println(result);
        System.out.println("== MyStreamV3 종료 ==");
    }

    private static void ex2(List<Integer> data) {
        System.out.println("== Stream API 시작 ==");
        Integer result = data.stream()
                .filter(n -> {
                    boolean isEven = n % 2 == 0;
                    System.out.println("fitler() 실행: " + n + "(" + isEven + ")");
                    return isEven;
                })
                .map(n -> {
                    int mapped = n * 10;
                    System.out.println("map() 실행: " + n + " -> " + mapped);
                    return mapped;
                })
                .findFirst().get();
        System.out.println(result);
        System.out.println("== Stream API 종료 ==");
    }
}
```
```
== MyStreamV3 시작 ==
fitler() 실행: 1(false)
fitler() 실행: 2(true)
fitler() 실행: 3(false)
fitler() 실행: 4(true)
fitler() 실행: 5(false)
fitler() 실행: 6(true)
map() 실행: 2 -> 20
map() 실행: 4 -> 40
map() 실행: 6 -> 60
20
== MyStreamV3 종료 ==
== Stream API 시작 ==
fitler() 실행: 1(false)
fitler() 실행: 2(true)
map() 실행: 2 -> 20
20
== Stream API 종료 ==
```
#### 정리
지연 연산과 파이프라인 구조를 이해하면, 스트림 API를 사용해 더 효율적이고 간결한 코드를 작성할 수 있게 된다.
* 불필요한 연산을 최소화 할 수 있다.
* 코드는 선언적이지만 내부적으로 효율적으로 동작한다.
