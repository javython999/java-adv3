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

# 스트림 API - 기능
## 스트림 생성
```java
public class CreateStreamMain {

    public static void main(String[] args) {
        System.out.println("1. 컬렉션으로부터 생성");
        List<String> list = List.of("a", "b", "c");
        Stream<String> stream1 = list.stream();
        stream1.forEach(System.out::println);

        System.out.println("2. 배열로부터 생성");
        String[] array = {"a", "b", "c"};
        Stream<String> stream2 = Arrays.stream(array);
        stream2.forEach(System.out::println);

        System.out.println("3. Stream.of() 사용");
        Stream<String> stream3 = Stream.of("a", "b", "c");
        stream3.forEach(System.out::println);

        System.out.println("4. 무한 스트림 생성 - iterate()");
        Stream<Integer> infiniteStream = Stream.iterate(0, n -> n + 2);
        infiniteStream.limit(10).forEach(System.out::println);

        System.out.println("5. 무한 스트림 생성 - generate()");
        Stream<Double> randomStream = Stream.generate(Math::random);
        randomStream.limit(3).forEach(System.out::println);
    }
}
```
#### 정리
* 컬렉션, 배열, Stream.of는 기본적으로 유한한 데이터 소스로부터 스트림을 생성한다.
* iterate, generate는 별도의 종료 조건이 없으면 무한히 데이터를 만들어 내는 스트림을 생성한다.
  * 필요한 만큼(limit) 사용해야 한다. 그렇지 않으면 무한 루프처럼 계속 스트림을 뽑아내므로 주의해야 한다.

## 중간 연산
```java
public class IntermediateOperationsMain {

    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 2, 3, 4, 5, 5, 6, 7, 8, 9, 10);

        // 1. filter
        System.out.println("1. filter - 짝수만 선택");
        numbers.stream()
                .filter(n -> n % 2 == 0)
                .forEach(n -> System.out.print(n + " "));
        System.out.println("\n");

        // 2. map
        System.out.println("2. map - 각 숫자를 제곱");
        numbers.stream()
                .map(n -> n * n)
                .forEach(n -> System.out.print(n + " "));
        System.out.println("\n");

        // 3. distinct
        System.out.println("3. distinct - 중복 제거");
        numbers.stream()
                .distinct()
                .forEach(n -> System.out.print(n + " "));
        System.out.println("\n");

        // 4. sorted (기본 정렬)
        System.out.println("4. sorted - 기본 정렬");
        Stream.of(3, 5, 1, 7, 2, 9, 4, 6, 8)
                .sorted()
                .forEach(n -> System.out.print(n + " "));
        System.out.println("\n");

        // 5. sorted (커스텀 정렬)
        System.out.println("4. sorted - 기본 정렬");
        Stream.of(3, 5, 1, 7, 2, 9, 4, 6, 8)
                .sorted(Comparator.reverseOrder())
                .forEach(n -> System.out.print(n + " "));
        System.out.println("\n");

        // 6. peak
        System.out.println("6. peek - 동작 확인용");
        numbers.stream()
                .peek(n -> System.out.print("before: " + n + ", "))
                .map(n -> n * n)
                .peek(n -> System.out.print("after: " + n + ", "))
                .limit(5)
                .forEach(n -> System.out.println("최종값: " + n));
        System.out.println("\n");

        // 7. limit
        System.out.println("7. limit - 처음 5개 요소만");
        numbers.stream()
                .limit(5)
                .forEach(n -> System.out.print(n + " "));
        System.out.println("\n");

        // 8 skip
        System.out.println("8. skip - 처음 5개 요소를 건너뛰기");
        numbers.stream()
                .skip(5)
                .forEach(n -> System.out.print(n + " "));
        System.out.println("\n");

        // 9. takeWhile (Java 9 +)
        List<Integer> numbers2 = List.of(1, 2, 3, 4, 5, 1, 2, 3);
        System.out.println("9. takeWhile - 5보다 작을 동안만 선택");
        numbers2.stream()
                .takeWhile(n -> n < 5)
                .forEach(n -> System.out.print(n + " "));
        System.out.println("\n");

        // 10. dropWhile (Java 9+)
        System.out.println("10. dropWhile - 5보다 작을 동안 건너뛰기");
        numbers2.stream()
                .dropWhile(n -> n < 5)
                .forEach(n -> System.out.print(n + " "));
    }
}
```
#### 정리
* 중간 연산은 파이프라인 형태로 연결할 수 있으며, 스트림을 변경하지만 원본 데이터 자체를 바꾸지 않는다.
* 중간 연산은 lazy하게 동작하므로, 최종 연산이 실행될 때까지 실제 처리는 일어나지 않는다.
* `peek`은 디버깅 목적으로 자주 사용하며, 실제 스트림의 요소값을 변경하거나 연산 결과를 반환하지 않는다.
* `takeWhile`, `dropWhile`은 자바 9부터 추가된 기능으로, 정렬된 스트림에서 사용할 때 유용하다.
  * 정렬되지 않은 스트림에서 쓰면 예측하기 어렵다.

## FlatMap
```java
public class MapVsFlatMapMain {

    public static void main(String[] args) {
        List<List<Integer>> outerList = List.of(
                List.of(1, 2),
                List.of(3 ,4),
                List.of(5, 6)
        );

        // forLoop
        List<Integer> forResult = new ArrayList<>();
        for (List<Integer> list: outerList) {
            for (Integer element: list) {
                forResult.add(element);
            }
        }
        System.out.println("forResult = " + forResult);

        // map
        List<Stream<Integer>> mapResult = outerList.stream()
                .map(list -> list.stream())
                .toList();
        System.out.println("mapResult = " + mapResult);

        // flatMap
        List<Integer> flatMapResult = outerList.stream()
                .flatMap(list -> list.stream())
                .toList();
        System.out.println("flatMapResult = " + flatMapResult);

    }
}
```
#### 정리
중첩 컬렉션을 다룰 때는 `map` 대신 `FlatMap`을 사용하면 중첩 컬렉션을 편리하게 하나의 컬렉션으로 변환할 수 있다.

## Optional
```java
package java.util;

public final class Optional<T> { 
    private final T value;
    ...
    public boolean isPresent() {
        return value != null;
    }
    
    public T get() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }
}
```
* `Optional`은 내부에 하나의 값(value)을 가진다.
* `isPresent()`를 통해 그 값(value)이 있는지 없는지 확인할 수 있다.
* `get()`을 통해 내부의 값을 꺼낼 수 있다. 만약 값이 없다면 예외가 발생한다.
* `Optional`은 이름 그대로 필수가 아니라 옵션이라는 뜻이다.

```java
public class OptionalSimpleMain {

    public static void main(String[] args) {
        Optional<Integer> optional1 = Optional.of(10);
        System.out.println("optional1 = " + optional1);

        if (optional1.isPresent()) {
            System.out.println("optional1.get() = " + optional1.get());
        }

        Optional<Object> optional2 = Optional.ofNullable(null);
        System.out.println("optional2 = " + optional2);

        if (optional2.isPresent()) {
            System.out.println("optional2.get() = " + optional2.get());
        }

        // 값이 없는 Optional에서 get()을 호출하면 NoSearchElementException 발생
        optional2.get();
    }
}
```
* `Optional`은 내부에 값을 담아두고, 그 값이 `null`이 아닌지를 체크할 수 있는 `isPresent()`와 같은 안전한 체크 메서드를 제공한다.
* `Optional`은 `null` 값으로 인한 오류(`NullPointerException`)를 방지하고, 코드에서 "값이 없을 수도 있다"는 상황을 명시적으로 표현하기 위해 사용된다.

## 최종 연산
```java
public class TerminalOperationsMain {

    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 2, 3, 4, 5, 5, 6, 7, 8, 9, 10);

        // Collectors 복잡한 수집이 필요할 때 사용
        System.out.println("1. collect - List 수집");
        List<Integer> evenNumbers1 = numbers.stream()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList());
        System.out.println("짝수 리스트: " + evenNumbers1);
        System.out.println();

        System.out.println("2. toList() - (Java 16+)");
        List<Integer> evenNumbers2 = numbers.stream()
                .filter(n -> n % 2 == 0)
                .toList();
        System.out.println("짝수 리스트: " + evenNumbers2);
        System.out.println();

        System.out.println("3. toArray() - 배열로 변환");
        Integer[] array = numbers.stream()
                .filter(n -> n % 2 == 0)
                .toArray(Integer[]::new);
        System.out.println("짝수 배열: " + Arrays.toString(array));

        System.out.println("4. forEach - 각 요소를 처리");
        numbers.stream()
                .limit(5)
                .forEach(n -> System.out.print(n + " "));
        System.out.println("\n");

        System.out.println("5. count - 요소 개수");
        long count = numbers.stream()
                .filter(n -> n > 5)
                .count();
        System.out.println("5보다 큰 숫자 개수: " + count);
        System.out.println();

        System.out.println("6. reduce - 요소들의 합");
        System.out.println("초기값이 없는 reduce");
        Optional<Integer> sum1 = numbers.stream()
                .reduce((a, b) -> a + b);
        System.out.println("합계(초기값 없음): " + sum1.get());
        System.out.println("초기값이 있는 reduce");
        int sum2 = numbers.stream()
                .reduce(100, (a, b) -> a + b);
        System.out.println("합계(초기값 없음): " + sum2);
        System.out.println("\n");

        System.out.println("7. min - 초소값");
        Optional<Integer> min = numbers.stream()
                .min(Integer::compareTo);
        System.out.println("최소값: " + min.get());
        System.out.println();

        System.out.println("8. max - 최대값");
        Optional<Integer> max = numbers.stream()
                .max(Integer::compareTo);
        System.out.println("최대값: " + max.get());
        System.out.println();

        System.out.println("9. findFirst - 첫 번째 요소");
        Integer first = numbers.stream()
                .filter(n -> n > 5)
                .findFirst().get();
        System.out.println("5 보다 큰 첫 번째 숫자: " + first);
        System.out.println();

        System.out.println("10. findAny - 아무 요소나 한 ㅏ찾기");
        Integer any = numbers.stream()
                .filter(n -> n > 5)
                .findAny().get();
        System.out.println("5 보다 큰 아무 숫자: " + any);
        System.out.println();

        System.out.println("11. anyMatch - 조건을 만족하는 요소 존재 여부");
        boolean hasEven = numbers.stream()
                .anyMatch(n -> n % 2 == 0);
        System.out.println("짝수가 있나? " + hasEven);
        System.out.println();

        System.out.println("12 allMatch - 모든 요소가 조건을 만족하는지");
        boolean allPositive = numbers.stream()
                .allMatch(n -> n > 0);
        System.out.println("모든 숫자가 양수 인가? " + allPositive);
        System.out.println();

        System.out.println("13. noneMatch - 조건을 만족하는 요소가 없는지");
        boolean noNegative = numbers.stream()
                .noneMatch(n -> n < 0);
        System.out.println("음수가 없나? " + noNegative);
    }
}
```
#### 정리
* 최종 연산이 호출되면, 그동안 정의된 모든 중간 연산이 한 번에 적용되어 결과를 만든다.
* 최종 연산을 한 번 수행하면, 스트림은 재사용할 수 없다.
* `reduce`를 사용할 때 초깃값을 지정하면, 스트림이 비어있어도 초기값이 결과가 된다. 초기값이 없으면 `Optional`을 반환한다.
* `findFirst()`, `findAny()`도 결과가 없을 수 있으므로 Optional을 통해 값 유무를 확인해야 한다.

## 기본형 특화 Stream
```java
public class PrimitiveStreamMain {

    public static void main(String[] args) {
        // 기본형 특화 스트림(IntStream, LongStream, DoubleStream)
        IntStream stream = IntStream.of(1, 2, 3, 4, 5);
        stream.forEach(i -> System.out.print(i + " "));
        System.out.println();

        // 범위 생성 메서드 (IntStream, LongStream)
        IntStream range1 = IntStream.range(1, 6); // [1, 2, 3, 4, 5]
        range1.forEach(i -> System.out.print(i + " "));
        System.out.println();
        IntStream range2 = IntStream.rangeClosed(1, 5); // [1, 2, 3, 4, 5]
        range2.forEach(i -> System.out.print(i + " "));
        System.out.println();

        // 1. 통계 관련 메서드(sum, average, min, max, count)
        // sum
        int sum = IntStream.range(1, 6).sum();
        System.out.println("sum = " + sum);

        // average
        double average = IntStream.range(1, 6).average().getAsDouble();
        System.out.println("average = " + average);

        // summaryStatistics
        IntSummaryStatistics stats = IntStream.range(1, 6).summaryStatistics();
        System.out.println("합계: " + stats.getSum());
        System.out.println("평균: " + stats.getAverage());
        System.out.println("최대값: " + stats.getMax());
        System.out.println("최소값: " + stats.getMin());
        System.out.println("개수: " + stats.getCount());

        // 2. 타입 변환 메서드
        // IntStream -> LongStream
        LongStream longStream = IntStream.range(1, 6).asLongStream();

        // IntStream -> DoubleStream
        DoubleStream doubleStream = IntStream.range(1, 6).asDoubleStream();

        // IntStream -> Stream<Integer>
        Stream<Integer> boxedStream = IntStream.range(1, 6).boxed();

        // 3. 기본형 특화 매핑
        // int -> long
        LongStream mappedLong = IntStream.range(1, 6).mapToLong(i -> i * 10L);

        // int -> double
        DoubleStream mappedToDouble = IntStream.range(1, 6).mapToDouble(i -> i * 1.5);

        // int -> 객체 변환 매핑
        Stream<String> mappedObj = IntStream.range(1, 6).mapToObj(i -> "Number: " + i);

        // 4. 객체 스트림 -> 기본형 특화 스트림으로 매핑
        Stream<Integer> integerStream = Stream.of(1, 2, 3, 4, 5);
        IntStream intStream = integerStream.mapToInt(i -> i);

        // 5. 객체 스트림 -> 기본형 특화 스트림으로 매핑 활용
        int result = Stream.of(1, 2, 3, 4, 5)
                .mapToInt(i -> i)
                .sum();
        System.out.println("result = " + result);

    }
}
```
#### 정리
* 기본형 특화 스트림(IntStream, LongStream, DoubleStream)을 이용하면 숫자 계산(합계, 평균, 최대, 최소 등)을 간편하게 처리하고 박싱/언박싱 오버헤드를 줄여 성능상의 이점도 얻을 수 있다.
* `range()`, `rangeClosed()` 같은 메서드를 사용하면 범위를 쉽게 다룰 수 있어 반복문 대신 자주 쓰인다.
* `mapToXxx`, `boxed()` 등의 메서드를 잘 활요하면 객체 스트림과 기본형 특화 스트림을 자유롭게 오가며 다양한 작업을 할 수 있다.
* `summaryStaticstics()`를 사용하면 합계, 평균, 최솟값, 최댓값 등 통계 정보를 한 번에 구할 수 있어 편리하다.

# 스트림 API - 컬렉터
## 컬렉터
스트림 중간 연산을 거쳐 최종 연산으로써 데이터를 처리할 때, 그 결과물이 필요한 경우가 많다.
리스트나 맵 같은 자료 구조로 답고 싶다거나 통계 데이터를 내고 싶다는 식의 요구가 있을때, 최종 연산에 `Collectors`를 활용한다.

```java
public static void main(String[] args) {
    // 기본 기능
    List<String> list = Stream.of("Java", "Spring", "JPA")
            .collect(Collectors.toList());
    System.out.println("list = " + list);

    // 수정 불가능 리스트
    List<Integer> unmodifiableNumberList = Stream.of(1, 2, 3)
            .collect(Collectors.toUnmodifiableList());
    //unmodifiableNumberList.add(4);
    System.out.println("unmodifiableNumberList = " + unmodifiableNumberList);

    Set<Integer> set = Stream.of(1, 2, 2, 3, 3)
            .collect(Collectors.toSet());
    System.out.println("set = " + set);

    // 타입 지정
    TreeSet<Integer> treeSet = Stream.of(3, 4, 5, 2, 1)
            .collect(Collectors.toCollection(TreeSet::new));
    System.out.println("treeSet = " + treeSet);
}
```
```java
public static void main(String[] args) {
    Map<String, Integer> map1 = Stream.of("Apple", "Banana", "Tomato")
            .collect(Collectors.toMap(
                    name -> name,
                    name -> name.length())
            );
    System.out.println("map1 = " + map1);

    // 키 중복 예외: java.lang.IllegalStateException: Duplicate key Apple
    /*
    Map<String, Integer> map2 = Stream.of("Apple", "Apple", "Tomato")
            .collect(Collectors.toMap(
                    name -> name,
                    name -> name.length())
            );
    System.out.println("map2 = " + map2);
    */

    Map<String, Integer> map3 = Stream.of("Apple", "Apple", "Tomato")
            .collect(Collectors.toMap(
                    name -> name,
                    name -> name.length(),
                    (oldValue, newValue) -> oldValue + newValue
            ));
    System.out.println("map3 = " + map3);

    // Map 타입 지정
    Map<String, Integer> map4 = Stream.of("Apple", "Apple", "Tomato")
            .collect(Collectors.toMap(
                    name -> name,
                    name -> name.length(),
                    (oldValue, newValue) -> oldValue + newValue,
                    LinkedHashMap::new
            ));
    System.out.println("map4 = " + map4.getClass());
}
```
```java
public static void main(String[] args) {
    Integer max1 = Stream.of(1, 2, 3)
            .collect(Collectors.maxBy((i1, i2) -> i1.compareTo(i2)))
            .get();
    System.out.println("max1 = " + max1);

    Integer max2 = Stream.of(1, 2, 3)
            .max((i1, i2) -> i1.compareTo(i2))
            .get();
    System.out.println("max2 = " + max2);

    Integer max3 = Stream.of(1, 2, 3)
            .max(Integer::compareTo)
            .get();
    System.out.println("max3 = " + max3);

    // 기본형 특화 스트림
    int max4 = IntStream.of(1, 2, 3)
            .max()
            .getAsInt();
    System.out.println("max4 = " + max4);
}
```
```java
public static void main(String[] args) {

    Long count1 = Stream.of(1, 2, 3)
            .collect(Collectors.counting());
    System.out.println("count1 = " + count1);

    long count2 = Stream.of(1, 2, 3)
            .count();
    System.out.println("count2 = " + count2);

    Double average1 = Stream.of(1, 2, 3)
            .collect(Collectors.averagingInt(i -> i));
    System.out.println("average1 = " + average1);

    // 기본형 특화 스트림으로 변환
    double average2 = Stream.of(1, 2, 3)
            .mapToInt(i -> i)
            .average()
            .getAsDouble();
    System.out.println("average2 = " + average2);

    // 기본형 특화 스트림 사용
    double average3 = IntStream.of(1, 2, 3)
            .average()
            .getAsDouble();
    System.out.println("average3 = " + average3);

    // 통계
    IntSummaryStatistics stats = Stream.of("Apple", "Banana", "Tomato")
            .collect(Collectors.summarizingInt(String::length));
    System.out.println(stats.getCount());
    System.out.println(stats.getSum());
    System.out.println(stats.getMin());
    System.out.println(stats.getMax());
    System.out.println(stats.getAverage());
}
```
```java
public static void main(String[] args) {
    List<String> names = List.of("a", "b", "c", "d");

    // 컬렉션의 리듀싱은 주로 다운 스트름에 활용
    String joined1 = names.stream()
            .collect(Collectors.reducing((s1, s2) -> s1 + ", " + s2))
            .get();
    System.out.println("joined1 = " + joined1);

    String joined2 = names.stream()
            .reduce((s1, s2) -> s1 + ", " + s2)
            .get();
    System.out.println("joined2 = " + joined2);

    // 문자열 전용 기능
    String joined3 = names.stream()
            .collect(Collectors.joining(", "));
    System.out.println("joined3 = " + joined3);

    String joined4 = String.join(", ", names);
    System.out.println("joined4 = " + joined4);
}
```

## 다운 스트림 컬렉터
#### 다운 스트림 컬렉터가 필요한 이유
* `groupingBy(...)`를 사용하면 일단 요소가 그룹별로 묶이지만, 그룹 내 요소를 구체적으로 어떻게 처리할지는 기본적으로 `toList()`만 적용된다.
* 그룹별 총합, 평균, 최소/최대값, 매핑된 결과, 통계 등을 바로 얻고 싶을 때가 많다.
* 다운 스트림 컬렉터를 활요하면 "그룹 내부"를 다시 한번 모으거나 집계하여 원하는 결과를 얻을 수 있다.

#### 다운 스트림 컬렉터란?
* `Collectors.groupingBy(...)` 또는 `Collectors.partitioningBy(...)`에서 두 번째 인자로 전달되는 `Collector`를 가리켜 "다운 스트림 컬렉터"라 한다.

```java
public static void main(String[] args) {

    List<Student> students = List.of(
            new Student("Kim", 1, 85),
            new Student("Park", 1, 70),
            new Student("Lee", 2, 70),
            new Student("Han", 2, 90),
            new Student("Hoon", 3, 90),
            new Student("Ha", 3, 89)
    );

    // 1. 학년 별로 학생을 그룹화
    Map<Integer, List<Student>> collect1_1 = students.stream()
            .collect(Collectors.groupingBy(Student::getGrade, Collectors.toList()));
    System.out.println("collect1_1 = " + collect1_1);

    // 다운스트림에서 toList() 생략 가능
    Map<Integer, List<Student>> collect1_2 = students.stream()
            .collect(Collectors.groupingBy(Student::getGrade));
    System.out.println("collect1_2 = " + collect1_2);

    // 2. 학년별로 학생들의 이름을 출력해라.
    Map<Integer, List<String>> collect2 = students.stream()
            .collect(Collectors.groupingBy(
                    Student::getGrade,
                    Collectors.mapping(
                            s -> s.getName(), // 다운스트림 1: 학생 -> 이름 변환
                            Collectors.toList())      // 다운스트림 1: 변환된 값(이름)을 list로 수집
            ));
    System.out.println("collect2 = " + collect2);

    // 3. 학년별로 학생들의 수를 출력
    Map<Integer, Long> collect3 = students.stream()
            .collect(Collectors.groupingBy(
                    Student::getGrade,
                    Collectors.counting())
            );
    System.out.println("collect3 = " + collect3);

    // 4. 학년별로 학생들의 평균 성적 출력
    Map<Integer, Double> collect4 = students.stream()
            .collect(Collectors.groupingBy(
                    Student::getGrade,
                    Collectors.averagingDouble(Student::getScore)
            ));
    System.out.println("collect4 = " + collect4);

}
```

```java
public static void main(String[] args) {

    List<Student> students = List.of(
            new Student("Kim", 1, 85),
            new Student("Park", 1, 70),
            new Student("Lee", 2, 70),
            new Student("Han", 2, 90),
            new Student("Hoon", 3, 90),
            new Student("Ha", 3, 89)
    );

   // 1. 학년별로 학생들을 그룹화
    Map<Integer, List<Student>> collect1 = students.stream()
            .collect(Collectors.groupingBy(Student::getGrade));
    System.out.println("collect1 = " + collect1);

    // 2. 학년별로 가장 점수가 높은 학생. reducing 사용
    Map<Integer, Optional<Student>> collect2 = students.stream()
            .collect(Collectors.groupingBy(
                            Student::getGrade,
                            Collectors.reducing((s1, s2) -> s1.getScore() > s2.getScore() ? s1 : s2)
                    )
            );
    System.out.println("collect2 = " + collect2);

    // 3. 학년별로 가장 점수가 높은 학생
    Map<Integer, Optional<Student>> collect3 = students.stream()
            .collect(Collectors.groupingBy(
                    Student::getGrade,
                    //Collectors.maxBy((s1, s2) -> s1.getScore() > s2.getScore() ? 1 : -1)
                    Collectors.maxBy(Comparator.comparingInt(Student::getScore))
            ));
    System.out.println("collect3 = " + collect3);

    // 4. 학년별로 가장 점수가 높은 학생의 이름 (collectingAndThen + maxBy)
    Map<Integer, String> collect4 = students.stream()
            .collect(Collectors.groupingBy(
                    Student::getGrade,
                    Collectors.collectingAndThen(
                            Collectors.maxBy(Comparator.comparingInt(Student::getScore)),
                            studentOptional -> studentOptional.get().getName()
                    )
            ));
    System.out.println("collect4 = " + collect4);
}
```
# Optional
## Optional이 필요한 이유
#### NullPointerException(NPE) 문제
* 자바에서 `null`은 값이 없음을 표현하는 가장 기본적인 방법이다.
* `null`을 잘못 사용하거나 `null` 참조에 대해 메서드를 호출하면 `NullPointerException`이 발생하여 프로그램이 예기치 않게 종료될 수 있다.
* `null` 체크가 누락되면, 추적하기 어렵고 디버깅 비용이 증가

#### 가독성 저하
* `null`을 반환하거나 사용하게 되면, 코드를 작성할 때마다  `if (obj != null) { ... } else { ... }` 같은 조건문으로 `null` 여부를 계속 확인해야 한다.
* `null` 체크 로직이 누적되면 코드가 복잡해지고 가독성이 떨어진다.

#### 의도가 드러나지 않음
* 메서드 시그니처만 보고서는 이 메서드가 `null`을 반환할 수 도 있다는 사실을 명확히 알기 어렵다.
* 호출하는 입장에서 "반드시 값이 존재할 것"이라고 가정했다가, 런타임에 `null`이 나와서 문제가 생길 수 있다.

#### Optional 미사용
```java
public class OptionalStartMain1 { 
    private static final Map<Long, String> map = new HashMap<>();
    
    static {
        map.put(1L, "Kim");
        map.put(2L, "Seo");
        // 3L은 넣지 않아서 찾을 수 없는 ID로 활용
    }
    
    public static void main(String[] args) {
        findAndPrint(1L); // 값이 있는 경우
        findAndPrint(3L); // 값이 없는 경우
    }
    
    // 이름이 있으면 이름을 대문자로 출력, 없으면 "UNKNOWN"을 출력해라.
    private static void findAndPrint(Long id) {
        String name = findNameById(id);
        // 1. NullPointerException 유발
        //System.out.println("name = " + name.toUpperCase());
        // 2. if 문을 활용한 null 체크 필요
        if (name != null) {
            System.out.println(id + ": " + name.toUpperCase());
        } else {
            System.out.println(id + ": " +  "UNKNOWN");
        }
    }
    
    private static String findNameById(Long id) {
        return map.get(id);
    }
}
```
#### Optional 사용
```java
public class OptionalStartMain2 {

    private static final Map<Long, String> map = new HashMap<>();

    static {
        map.put(1L, "Kim");
        map.put(2L, "Seo");
    }

    public static void main(String[] args) {
        findAndPrint(1L);
        findAndPrint(3L);
    }

    private static void findAndPrint(Long id) {
        String name = findNameById(id).orElse("UNKNOWN");
        System.out.println(id +": " + name.toUpperCase());
    }

    private static Optional<String> findNameById(Long id) {
        return Optional.ofNullable(map.get(id));
    }
}
```
코드가 더 간결해지고 반환타입을 통해 반환값이 `null`일 수도 있음을 알 수 있다.
## Optional의 생성과 값 획득
#### Optional 생성
```java
    public static void main(String[] args) {
        // 1. of() : 값이 null이 아님이 확실할 때 사용, null이면 NullPointerException 발생
        String noneNullValue = "Hello World";
        Optional<String> opt1 = Optional.of(noneNullValue);
        System.out.println("opt1 = " + opt1);

        // 2. ofNullable() : 값이 null일 수도, 아닐 수도 있을 때
        Optional<String> opt2 = Optional.ofNullable("Hello");
        Optional<String> opt3 = Optional.ofNullable(null);
        System.out.println("opt2 = " + opt2);
        System.out.println("opt3 = " + opt3);

        // 3 empty() : 비어있는 Optional을 명시적으로 생성
        Optional<Object> opt4 = Optional.empty();
        System.out.println("opt4 = " + opt4);
    }
```
#### Optional 값 획득
```java
    public static void main(String[] args) {

        // 예제: 문자열 "Hello"가 있는 Optional과 비어있는 Optional 준비
        Optional<String> optValue = Optional.of("Hello");
        Optional<Object> optEmpty = Optional.empty();

        // isPresent() : 값이 있으면 true
        System.out.println("=== 1. isPresent() / isEmpty() ===");
        System.out.println("optValue.isPresent() = " + optValue.isPresent());
        System.out.println("optEmpty.isPresent() = " + optEmpty.isPresent());
        System.out.println("optValue.isEmpty() = " + optValue.isEmpty());
        System.out.println("optEmpty.isEmpty() = " + optEmpty.isEmpty());

        // get() : 직접 내부 값을 꺼냄, 값이 없으면 예외(NoSuchElementException)
        System.out.println("=== 2. get() ===");
        String getValue = optValue.get();
        System.out.println("getValue = " + getValue);
        //String getValue2 = optEmpty.get();

        // 값이 있으면 그 값, 없으면 지정된 기본값 사용
        System.out.println("=== 3. orElse() ===");
        String value1 = optValue.orElse("기본값");
        Object empty1 = optEmpty.orElse("기본값");
        System.out.println("value1 = " + value1);
        System.out.println("value2 = " + empty1);

        // 값이 없을 때만 람다(Supplier)가 실행되어 기본값 생성
        System.out.println("=== 4. orElseGet() ===");
        String value2 = optValue.orElseGet(() -> {
            System.out.println("람다 호출 - optValue");
            return "New Value";
        });
        String empty2 = optEmpty.orElseGet(() -> {
            System.out.println("람다 호출 - optEmpty");
            return "New Value";
        }).toString();
        System.out.println("value2 = " + value2);
        System.out.println("empty2 = " + empty2);

        // 값이 있으면 반환, 없으면 예외 발생
        System.out.println("=== 5. orElseThrow() ===");
        String value3 = optValue.orElseThrow(() -> new RuntimeException("값이 없습니다."));
        System.out.println("value3 = " + value3);

        try {
            Object empty3 = optEmpty.orElseThrow(() -> new RuntimeException("값이 없습니다."));
            System.out.println("empty3 = " + empty3);
        } catch (RuntimeException e) {
            System.out.println("예외 발생" + e.getMessage());
        }

        // Optional을 반환
        System.out.println("=== 6. or() ===");
        Optional<String> result1 = optValue.or(() -> Optional.of("Fallback"));
        System.out.println("result1 = " + result1);
        Optional<Object> result2 = optEmpty.or(() -> Optional.of("Fallback"));
        System.out.println("result2 = " + result2);

    }
```

## Optional 값 처리
`Optional`에서는 값이 존재할 때와 존재하지 않을 때를 처리하기 위한 다양한 메서드들을 제공한다.
이를 활용하면 `null` 체크 로직 없이도 안전하고 간결하게 값을 다룰 수 있다.

#### Optional 값 처리 메서드
```java
public static void main(String[] args) {
    Optional<String> optValue = Optional.of("Hello");
    Optional<String> optEmpty = Optional.empty();

    // 값이 존재하면 Consumer 실행, 없으면 아무 일도 하지 않음
    System.out.println("=== 1. ifPresent() ===");
    optValue.ifPresent(v -> System.out.println("optValue 값 : " + v));
    optEmpty.ifPresent(v -> System.out.println("optValue 값 : " + v));

    // 값이 있으면 Consumer 실행, 없으면 Runnable 실행
    System.out.println("=== 2. ifPresentOrElse() ===");
    optValue.ifPresentOrElse(
            v -> System.out.println("optValue 값 : " + v),
            () -> System.out.println("optValue는 비어있음.")
    );
    optEmpty.ifPresentOrElse(
            v -> System.out.println("optEmpty 값 : " + v),
            () -> System.out.println("optEmpty는 비어있음.")
    );

    // 값이 있으면 Function 적용후 Optional로 반환, 없으면 Optional.empty()
    System.out.println("=== 3. map() ===");
    Optional<Integer> lengthOpt1 = optValue.map(String::length);
    System.out.println("optValue.map(String::length) = " + lengthOpt1);
    Optional<Integer> lengthOpt2 = optEmpty.map(String::length);
    System.out.println("optEmpty.map(String::length) = " + lengthOpt2);

    // map()과 유사하나, 이미 Optional을 반환하는 경우 중첩을 제거
    System.out.println("== 4. flatMap() ===");
    System.out.println("[Map]");
    Optional<Optional<String>> nestedOpt = optValue.map(s -> Optional.of(s));
    System.out.println("nestedOpt = " + nestedOpt);
    System.out.println("[FlatMap]");
    Optional<String> flattenedOpt = optValue.flatMap(o -> Optional.of(o));
    System.out.println("flattenedOpt = " + flattenedOpt);

    // 값이 있고 조건을 만족하면 그 값을 그대로, 불만족시 Optional.empty()
    System.out.println("== 5. filter() ===");
    Optional<String> filtered1 = optValue.filter(s -> s.startsWith("H"));
    Optional<String> filtered2 = optValue.filter(s -> s.startsWith("X"));
    System.out.println("filtered1 = " + filtered1);
    System.out.println("filtered2 = " + filtered2);

    // 값이 있으면 단일 요소 스트림, 없으면 빈 스트림
    System.out.println("== 6. stream() ===");
    optValue.stream()
            .forEach(System.out::println);
    optEmpty.stream()
            .forEach(System.out::println);

}
```
## 즉시 평가와 지연평가
* 즉시 평가(eager evaluation): 값(혹은 객체)을 바로 생성하거나 계산해 버리는 것
* 지연 평가(lazy evaluation): 값이 실제 필요로 할때(즉, 사용될 때)까지 계산을 미루는 것

```java
public static void main(String[] args) {
    Logger logger = new Logger();
    logger.setDebug(true);
    logger.debug(value100() + value200());

    System.out.println("=== 디버그 모드 끄기 ===");
    logger.setDebug(false);
    logger.debug(value100() + value200());

    System.out.println("=== 디버그 모드 체크 ===");
    if (logger.isDebug()) {
        logger.debug(value100() + value200());
    }
}

private static int value100() {
    System.out.println("value100 호출");
    return 100;
}

private static int value200() {
    System.out.println("value200 호출");
    return 200;
}
```

자바에서 연산을 정의하는 시점과 해당 연산을 실행하는 시점을 분리하는 방법은 여러가지가 있다.
* 익명 클래스를 만들고, 메서드를 나중에 호출
* 람다를 만들고, 해당 람다를 나중에 호출
```java
public static void main(String[] args) {
    Logger logger = new Logger();
    logger.setDebug(true);
    logger.debug(() -> value100() + value200());

    System.out.println("=== 디버그 모드 끄기 ===");
    logger.setDebug(false);
    logger.debug(() -> value100() + value200());

    System.out.println("=== 디버그 모드 체크 ===");
    if (logger.isDebug()) {
        logger.debug(() -> value100() + value200());
    }
}

private static int value100() {
    System.out.println("value100 호출");
    return 100;
}

private static int value200() {
    System.out.println("value200 호출");
    return 200;
}
```


#### 정리
람다를 사용해서 연산을 정의하는 시점과 실행(평가)하는 시점을 분리하면 꼭 필요한 계산만 처리할 수 있다.

## orElse() vs orElseGet()
```java
public class OrElseGetMain {

    public static void main(String[] args) {
        Optional<Integer> optValue = Optional.of(100);
        Optional<Integer> optEmpty = Optional.empty();

        System.out.println("단순 계산");
        Integer i1 = optValue.orElse(10 + 20);
        Object i2 = optEmpty.orElse(10 + 20);
        System.out.println("i1 = " + i1);
        System.out.println("i2 = " + i2);

        // 값이 있으면 그 값, 없으면 지정된 기본값 사용
        System.out.println("=== orElse ===");
        System.out.println("값이 있는 경우");
        Integer value1 = optValue.orElse(createData());
        System.out.println("value1 = " + value1);

        System.out.println("값이 없는 경우");
        Integer empty1 = optEmpty.orElse(createData());
        System.out.println("empty1 = " + empty1);

        // 값이 있으면 그 값, 없으면 지정된 람다 사용
        System.out.println("=== orElseGet ===");
        System.out.println("값이 있는 경우");
        Integer value2 = optValue.orElseGet(() -> createData());
        System.out.println("value2 = " + value2);

        System.out.println("값이 없는 경우");
        Integer empty2 = optEmpty.orElseGet(() -> createData());
        System.out.println("empty2 = " + empty2);

    }

    private static int createData() {
        System.out.println("데이터를 생성합니다...");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        int createValue = new Random().nextInt(100);
        System.out.println("데이터 생성이 완료되었습니다. 생성값 : " + createValue);
        return createValue;

    }
}
```
* `orElse(T other)`는 빈 값이면 `other`를 반환하는데 `other`는 항상 미리 계산한다.
  * 따라서 `other`를 생성하는 비용이 큰 경우 실제로 값이 있을 때도 쓸데없이 생성 로직이 실행될 수 있다.
  * `orElse()`에 넘기는 표현식은 호출 즉시 평가 하므로 즉시 평가가 적용된다.
* `orElseGet(Supplier suppliter)`은 빈 값이면 `supplier`를 통해 생성하기 때문에 값이 있을 때는 `supplier`를 호출 되지 않는다.
  * 생성 비용이 높은 객체를 다룰 때는 `orElseGet()`이 더 효율적이다.
  * `orElseGet()`에 넘기는 표현식은 필요할 때만 평가하므로 지연 평가가 적용된다.

단순한 대체 값을 전달하거나 코드가 매우 간단하다면 `orElse()`를 사용하고,
객체 생성 비용이 큰 로직이 들어있고, Optional에 값이 이미 존재할 가능성이 높다면 `orElseGet()`을 고려해볼 수 있다.

## Optional best practice
#### 1. 반환 타입으만 사용하고, 필드에는 가급적 쓰지말기
* `Optional`은 주로 메서드의 반환 값에대해 "값이 없을 수도 있음"을 표현하기 위해 도입되었다.
* 클래스의 필드에 `Optional`을 직접 두는 것은 권장하지 않는다.

#### 잘못 사용 예시
```java
public class Product { 
    // 안티 패턴: 필드를 Optional로 선언
    private Optional<String> name; 
    // ... constructor, getter, etc.
}
```

#### 권장 예시
```java
public class Product {
    // 필드는 원시 타입(혹은 일반 참조 타입) 그대로 둔다.
    private String name;
    // ... constructor, getter, etc.

    // name 값을 가져올 때, "필드가 null일 수도 있음"을 고려해야 한다면
    // 다음 메서드에서 Optional로 변환해서 반환할 수 있다.
    public Optional<String> getNameAsOptional() {
        return Optional.ofNullable(name);
    }
}
```

#### 2. 메서드 매개변수로 `Optional`을 사용하지 말기
* 자바 공식 문서에 `Optional`은 메서드의 반환값으로 사용하기를 권장하며 매개변수로 사용하지 말라고 명시되어있다.
* 호출하는 측에서는 단순히 `null` 전달 대신 `Optional.empty()`를 전달해야 하는 부담이 생기며, 결국 `null`을 사용하든 `Optional.empty()`를 사용하든 큰 차이가 없어 가독성만 떨어진다.

#### 잘못 사용 예시
```java
public void processOrder(Optional<Long> orderId) {
    if (orderId.isPresent()) {
        System.out.println("Order ID:" + orderId.get());
    } else {
        System.out.println("Order ID is Empty()");
    }
}
```

#### 권장 예시
* 오버로드 된 메서드를 만드는 경우
* 명시적으로 `null` 허용 여부를 문서화하는 방식을 택한다.

```java
public void processOrder(Long orderId) {
    System.out.println("Order ID:" + orderId);
}

public void processOrder() {
    System.out.println("Order ID is Empty()");
}


public void processOrder(Long orderId) {
    if (orderId == null) {
        System.out.println("Order ID is empty!");
        return;
    }
    System.out.println("Order ID: " + orderId);
}
```

#### 컬렉션(Collection)이나 배열 타입을 `Optional`로 감싸지 말기
* `List<T>`, `Set<T>`, `Map<K, V>` 등 컬렉션 자체는 비어있는 상태(empty)를 표현할 수 있다.
* `Collection.emptyList()`를 사용하면 된다.

#### 잘못 사용 예시
```java
public Optional<List<String>> getUserRoles(String userId) {
    List<String> userRolesList ...;
    if (foundUser) {
        return Optional.of(userRolesList);
    } else {
        return Optional.empty();
    }
}

Optional<List<String>> optList = getUserRoles("someUser");
    if (optList.isPresent()){
        // ...
    }
```

#### 권장 예시
```java
public List<String> getUserRoles(String userId) {
    // ...
    if (!foundUser) {
        // 권장: 빈 리스트 반환
        return Collections.emptyList();
    }
    return userRolesList;
}
```

#### 4. isPresent()와 get() 조합을 직접 사용하지 않기
* `Optional`의 `get()` 메서드는 가급적 사용하지 않아야 한다.
* `if (opt.isPresent()) { opt.get() } else { ... }`는 사실상 `null` 체크와 다를바 없다
* 깜빡하면 `NoSearchElementException` 같은 예외가 발생할 수 있다.
* 대신 `orElse`, `orElseGet`, `orElseThrow`, `ifPresentOrElse`, `map`, `filter` 등의 메서드를 활용하라

#### 잘못 사용 예시
```java
public static void main(String[] args) {
    Optional<String> optStr = Optional.ofNullable("Hello");
    if (optStr.isPresent()) {
        System.out.println(optStr.get());
    } else {
        System.out.println("Nothing");
    }
}
```
#### 권장 예시
```java
public static void main(String[] args) {
    Optional<String> optStr = Optional.ofNullable("Hello");

    // 1) orElse
    System.out.println(optStr.orElse("Nothing"));

    // 2) ifPresentOrElse
    optStr.ifPresentOrElse(
            System.out::println,
            () -> System.out.println("Nothing")
    );

    // 3) map
    int length = optStr.map(String::length).orElse(0);
    System.out.println("Length: " + length);
}
```

#### 5. `orElseGet()` vs `orElse()` 차이를 분명히 이해하기
* `orElse(T other)`는 항상 `other`를 즉시 평가한다.
* `orElseGet(Supplier<? extends T> supplier)`는 지연 평가한다.

#### 6. 무조건 Optional이 좋은 것은 아니다.
* `Optional`은 분명히 편의성과 안전성을 높여주지만, 모든 곳에서 "무조건" 사용하는 것은 오히려 코드 복잡성을 증가 시킬 수 있다.
* 다음과 같은 `Optional` 사용이 오히려 불필요할 수 있다.
  1. 항상 값이 있는 상황
  2. 값이 없으면 예외를 던지는 것이 더 자연스러운 상황
  3. 흔히 비는 경우가 아니라 흔히 채워져 있는 경우
  4. 성능이 극도로 중요한 로우레벨 코드

# 디폴트 메서드
## 디폴트 메서드가 등장한 이유
자바 8부터는 인터페이스에 메서드 본문을 가질 수 있도록 허용해 주어, 기존 코드를 깨뜨리지 않고 새 기능을 추가할 수 있게 되었다.

자바 8 이전까지는 인터페이스에 새로운 메서드를 추가하면, 해당 인터페이스를 구현한 모든 클래스에서 그 메서드를 구현해야 했다.
인터페이스의 이런 엄격한 규칙 때문에, 그 동안 자바 인터페이스에 새로운 기능을 추가하지 못하는 일이 발생하게 되었다.

## 디폴트 메서드 소개
디폴트 메서드의 도입 이유
* 하위 호환성 보장
  * 인터페이스에 새로운 메서드가 추가되더라도, 기존 코드가 깨지지 않도록 하기 위한 목적으로 디폴트 메서드가 도입되었다.
* 라이브러리 확장성
  * 사용자들이 서드파티 라이브러리 구현체가 일일이 수정하지 않아도 되도록 만들었다.
* 람다와 스트림 API 연계
  * 자바 8에서 함께 도임된 람다와 스트림 API를 보다 편리하게 활용하기 위해 인터페이스에 구현 로직을 제공할 필요가 있었다.
* 설계 유연성 향상
  * 디폴트 메서드를 통해 인터페이스에서도 일부 공통 동작 방식을 정의할 수 있게 되었다.

## 디폴트 메서드의 올바른 사용법
1. 하위 호환성을 위해 최소한으로 사용
   * 디폴트 메서드는 주로 이미 배포된 인터페이스에 새로운 메서드를 추가하면 기존 구현체 코드를 깨뜨리지 않기 위한 목적으로 만들어졌다.
   * 새 메서드가 필요한 상황이고, 기존 구현 클래스가 많은 상황이 아니라면, 원칙적으로는 각각 구현하거나, 또는 추상 메서드를 추가하는 것을 고려하자.
   * 불필요한 디폴트 메서드 남용은 코드 복잡도를 높일 수 있다.
2. 인터페이스는 여전히 추상화의 역할
   * 디폴트 메서드는 어디까지나 하위 호환을 위한 기능이나, 공통으로 쓰기 쉬운 간단한 로직을 제공하는 정도가 이상적이다.
3. 다중 상속(충돌) 문제
   * 하나의 클래스가 여러 인터페이스를 동시에 구현하는 상황에서, 서로 다른 인터페이스에 동일한 시그니처의 디폴트 메서드가 존재하면 충돌이 일어난다.
   * 이 경우 구현 클래스에 반드시 메서드를 재정의 해야 한다. 그리고 직접 구현 로직을 작성하거나 또는 어떤 인터페이스의 디폴트 메서드를 쓸 것인지 명시해 주어야 한다.
4. 디폴트 메서드에 상태(state)를 두지 않기
   * 인터페이스는 일반적으로 상태 없이 동작만 정의하는 추상화 계층이다.
   * 인터페이스에 정의하는 디폴트 메서드도 "구현"을 일부 제공할 뿐, 인스턴스 변수를 활용하거나 여러 차례 호출시 상태에 따라 동작이 달라지는 등의 동작은 지양해야 한다.
   * 이런 로직이 필요하다면 클래스(추상 클래스 등)로 옮기는 것이 더 적절하다.

# 병렬 스트림
## 단일 스트림
```java
public class ParallelMain1 {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        int sum = IntStream.rangeClosed(1, 8)
                .map(HeavyJob::heavyTask)
                .sum();

        long endTime = System.currentTimeMillis();
        log("time: " + (endTime - startTime) + "ms, sum: " + sum);
    }
}
```
```
11:27:26.342 [     main] calculate 1 -> 10
11:27:27.357 [     main] calculate 2 -> 20
11:27:28.357 [     main] calculate 3 -> 30
11:27:29.372 [     main] calculate 4 -> 40
11:27:30.375 [     main] calculate 5 -> 50
11:27:31.377 [     main] calculate 6 -> 60
11:27:32.390 [     main] calculate 7 -> 70
11:27:33.404 [     main] calculate 8 -> 80
11:27:34.409 [     main] time: 8082ms, sum: 360
```
## 스레드 직접 사용
```java
public class ParallelMain2 {

    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();

        // 1. Fork 작업을 분할한다.
        SumTask task1 = new SumTask(1, 4);
        SumTask task2 = new SumTask(5, 8);

        Thread thread1 = new Thread(task1, "thread-1");
        Thread thread2 = new Thread(task2, "thread-2");

        // 2. 분할한 작업을 처리한다.
        thread1.start();
        thread2.start();

        // 3. joni - 처리한 결과를 합친다.
        thread1.join();
        thread2.join();
        log("main 스레드 대기 완료");
        int sum = task1.result + task2.result;
        
        long endTime = System.currentTimeMillis();
        log("time: " + (endTime - startTime) + "ms, sum: " + sum);
    }

    private static class SumTask implements Runnable {
        int startValue;
        int endValue;
        int result = 0;

        public SumTask(int startValue, int endValue) {
            this.startValue = startValue;
            this.endValue = endValue;
        }

        @Override
        public void run() {
            log("작업 시작");
            int sum = 0;
            for (int i = startValue; i <= endValue; i++) {
                int calculated = HeavyJob.heavyTask(i);
                sum += calculated;
            }
            result = sum;
            log("작업 완료 result = " + result);
        }
    }
}
```
```
11:34:15.446 [ thread-1] 작업 시작
11:34:15.446 [ thread-2] 작업 시작
11:34:15.455 [ thread-2] calculate 5 -> 50
11:34:15.456 [ thread-1] calculate 1 -> 10
11:34:16.457 [ thread-1] calculate 2 -> 20
11:34:16.457 [ thread-2] calculate 6 -> 60
11:34:17.460 [ thread-1] calculate 3 -> 30
11:34:17.460 [ thread-2] calculate 7 -> 70
11:34:18.464 [ thread-2] calculate 8 -> 80
11:34:18.464 [ thread-1] calculate 4 -> 40
11:34:19.479 [ thread-2] 작업 완료 result = 260
11:34:19.479 [ thread-1] 작업 완료 result = 100
11:34:19.480 [     main] main 스레드 대기 완료
11:34:19.482 [     main] time: 4050ms, sum: 360
```
## 스레드 풀 사용
```java
public class ParallelMain3 {

    public static void main(String[] args) {
        // 스레드풀 준비
        try (ExecutorService es = Executors.newFixedThreadPool(2)) {
            // 1. Fork 작업을 분할한다.
            long startTime = System.currentTimeMillis();

            SumTask task1 = new SumTask(1, 4);
            SumTask task2 = new SumTask(5, 8);

            // 2. 분할한 작업을 처리한다.
            Future<Integer> future1 = es.submit(task1);
            Future<Integer> future2 = es.submit(task2);

            // 3. join - 처리한 결과를 합친다. get: 결과가 나올 때까지 대기한다.
            Integer result1 = future1.get();
            Integer result2 = future2.get();
            int sum = result1 + result2;

            long endTime = System.currentTimeMillis();
            log("time: " + (endTime - startTime) + "ms, sum: " + sum);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private static class SumTask implements Callable<Integer> {
        int startValue;
        int endValue;

        public SumTask(int startValue, int endValue) {
            this.startValue = startValue;
            this.endValue = endValue;
        }

        @Override
        public Integer call() {
            log("작업 시작");
            int sum = 0;
            for (int i = startValue; i <= endValue; i++) {
                int calculated = HeavyJob.heavyTask(i);
                sum += calculated;
            }
            return sum;
        }
    }
}
```
```
11:41:30.873 [pool-1-thread-1] 작업 시작
11:41:30.873 [pool-1-thread-2] 작업 시작
11:41:30.883 [pool-1-thread-1] calculate 1 -> 10
11:41:30.883 [pool-1-thread-2] calculate 5 -> 50
11:41:31.892 [pool-1-thread-2] calculate 6 -> 60
11:41:31.892 [pool-1-thread-1] calculate 2 -> 20
11:41:32.906 [pool-1-thread-2] calculate 7 -> 70
11:41:32.906 [pool-1-thread-1] calculate 3 -> 30
11:41:33.907 [pool-1-thread-2] calculate 8 -> 80
11:41:33.907 [pool-1-thread-1] calculate 4 -> 40
11:41:34.922 [     main] time: 4064ms, sum: 360
```
## Fork/Join 패턴
분할(Fork), 처리(Execute), 모음(Join)
이렇게 분할(Fork) -> 처리(Execute) -> 모음(Join)의 단계로 이루어진 멀티스레딩 패턴을 Fork/Join 패턴이라고 부른다.
이 패턴은 병렬 프로그래밍에서 매우 효율적인 방식으로, 복잡한 작업을 병렬적으로 처리할 수 있게 해준다.

## Fork/Join 프레임워크 - 소개
자바의 Fork/Join 프레임워크는 자바 7부터 도입된 `java.util.concurrent` 패키지의 일부로, 멀티코어 프로세서를 효율적으로 활요하기 위한 병렬 처리 프레임워크이다.

#### 분할 정복(Divide and Conquer) 전략
* 큰 작업을 작은 단뒤로 재귀적으로 분할
* 각 작은 작업은 결과를 합쳐 최종 결과를 생성
* 멀티코어 환경에서 작업을 효율적으로 분산 처리

#### 작업 훔치기(Work Stealing) 알고리즘
* 각 스레드는 자신의 작업 큐를 가짐
* 작업이 없는 스레드는 다른 바쁜 스레드의 큐에서 작업을 훔쳐와서 대신 처리
* 부하 균현을 자동으로 조절하여 효율 향상

#### 주요 클래스
* `ForkJoinPool`
* `ForkJoinTask`
  * `RecursiveTask`
  * `RecursiveAction`

#### ForkJoinPool
* Fork/Join 작업을 수행하는 특수한 `ExecutorService` 스레드 풀
* 작업 스케줄링 및 스레드 관리를 담당
* 기본적으로 사용 가능한 프로세서 수 만큼 스레드 생성

#### ForkJoinTask
* `ForkJoinTask`는 Fork/Join 작업의 기본 추상 클래스다.
* `Future`를 구현했다.
* 개발자는 주로 다음 두 하위 클래스를 구현해서 사용한다.
  * `RecursiveTask<V>`: 결과를 반환하는 작업
  * `RecursiveAction`: 결과를 반환하지 않는 작업(void)

#### RecursiveTask / RecursiveAction
* `compute()` 메서드를 재정의해서 필요한 작업 로직을 작성한다.
* 일반적으로 일정 기준값(임계값)을 두고, 작업 범위가 작으면 직접 처리하고, 크면 작업을 둘로 분할하여 각각 병렬로 처리하도록 구현한다.

#### fork() / join() 메서드
* `fork()`: 현재 스레드에서 다른 스레드로 작업을 분할하여 보내는 동작(비동기 실행)
* `join()`: 분할된 작업이 끝날 때까지 기다린 후 결과를 가져오는 동작

```java
public class ForkJoinMain1 {

    public static void main(String[] args) {
        List<Integer> data = IntStream.rangeClosed(1, 8)
                .boxed()
                .toList();

        log("[생성] " + data);

        try (ForkJoinPool pool = new ForkJoinPool(10)) {
            // ForkJoinPool 생성 및 작업 수행
            long startTime = System.currentTimeMillis();

            SumTask task = new SumTask(data);

            // 병렬로 합을 구한후 결과 출력
            Integer sum = pool.invoke(task);

            long endTime = System.currentTimeMillis();
            log("time: " + (endTime - startTime) + "ms, sum: " + sum);
            log("pool " + pool);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
```
```
12:09:56.941 [     main] [생성] [1, 2, 3, 4, 5, 6, 7, 8]
12:09:56.954 [ForkJoinPool-1-worker-1] [분할] [1, 2, 3, 4, 5, 6, 7, 8] -> left: [1, 2, 3, 4], right: [5, 6, 7, 8]
12:09:56.954 [ForkJoinPool-1-worker-1] [처리 시작] [5, 6, 7, 8]
12:09:56.954 [ForkJoinPool-1-worker-2] [처리 시작] [1, 2, 3, 4]
12:09:56.963 [ForkJoinPool-1-worker-2] calculate 1 -> 10
12:09:56.963 [ForkJoinPool-1-worker-1] calculate 5 -> 50
12:09:57.968 [ForkJoinPool-1-worker-1] calculate 6 -> 60
12:09:57.968 [ForkJoinPool-1-worker-2] calculate 2 -> 20
12:09:58.969 [ForkJoinPool-1-worker-2] calculate 3 -> 30
12:09:58.969 [ForkJoinPool-1-worker-1] calculate 7 -> 70
12:09:59.970 [ForkJoinPool-1-worker-2] calculate 4 -> 40
12:09:59.970 [ForkJoinPool-1-worker-1] calculate 8 -> 80
12:10:00.976 [ForkJoinPool-1-worker-1] [처리 완료] [5, 6, 7, 8] -> sum: 260
12:10:00.976 [ForkJoinPool-1-worker-2] [처리 완료] [1, 2, 3, 4] -> sum: 100
12:10:00.978 [ForkJoinPool-1-worker-1] LEFT[ [1, 2, 3, 4] ] + RIGHT[ [5, 6, 7, 8] ] = 360
12:10:00.979 [     main] time: 4034ms, sum: 360
12:10:00.979 [     main] pool java.util.concurrent.ForkJoinPool@439f5b3d[Running, parallelism = 10, size = 3, active = 0, running = 0, steals = 2, tasks = 0, submissions = 0]
```

## Fork/Join 프레임워크 - 작업 훔치기
```java
public class SumTask extends RecursiveTask<Integer> {
    //private static final int THRESHOLD = 4; // 임계값
    private static final int THRESHOLD = 2; // 임계값 변경
    ...
}
```
```
12:17:42.471 [     main] [생성] [1, 2, 3, 4, 5, 6, 7, 8]
12:17:42.486 [ForkJoinPool-1-worker-1] [분할] [1, 2, 3, 4, 5, 6, 7, 8] -> left: [1, 2, 3, 4], right: [5, 6, 7, 8]
12:17:42.487 [ForkJoinPool-1-worker-1] [분할] [5, 6, 7, 8] -> left: [5, 6], right: [7, 8]
12:17:42.488 [ForkJoinPool-1-worker-2] [분할] [1, 2, 3, 4] -> left: [1, 2], right: [3, 4]
12:17:42.488 [ForkJoinPool-1-worker-1] [처리 시작] [7, 8]
12:17:42.488 [ForkJoinPool-1-worker-3] [처리 시작] [5, 6]
12:17:42.488 [ForkJoinPool-1-worker-2] [처리 시작] [3, 4]
12:17:42.488 [ForkJoinPool-1-worker-4] [처리 시작] [1, 2]
12:17:42.500 [ForkJoinPool-1-worker-4] calculate 1 -> 10
12:17:42.500 [ForkJoinPool-1-worker-1] calculate 7 -> 70
12:17:42.500 [ForkJoinPool-1-worker-2] calculate 3 -> 30
12:17:42.500 [ForkJoinPool-1-worker-3] calculate 5 -> 50
12:17:43.502 [ForkJoinPool-1-worker-4] calculate 2 -> 20
12:17:43.502 [ForkJoinPool-1-worker-3] calculate 6 -> 60
12:17:43.502 [ForkJoinPool-1-worker-1] calculate 8 -> 80
12:17:43.502 [ForkJoinPool-1-worker-2] calculate 4 -> 40
12:17:44.514 [ForkJoinPool-1-worker-4] [처리 완료] [1, 2] -> sum: 30
12:17:44.514 [ForkJoinPool-1-worker-2] [처리 완료] [3, 4] -> sum: 70
12:17:44.514 [ForkJoinPool-1-worker-1] [처리 완료] [7, 8] -> sum: 150
12:17:44.514 [ForkJoinPool-1-worker-3] [처리 완료] [5, 6] -> sum: 110
12:17:44.517 [ForkJoinPool-1-worker-1] LEFT[ [5, 6] ] + RIGHT[ [7, 8] ] = 260
12:17:44.517 [ForkJoinPool-1-worker-2] LEFT[ [1, 2] ] + RIGHT[ [3, 4] ] = 100
12:17:44.517 [ForkJoinPool-1-worker-1] LEFT[ [1, 2, 3, 4] ] + RIGHT[ [5, 6, 7, 8] ] = 360
12:17:44.519 [     main] time: 2042ms, sum: 360
12:17:44.520 [     main] pool java.util.concurrent.ForkJoinPool@439f5b3d[Running, parallelism = 10, size = 4, active = 0, running = 0, steals = 4, tasks = 0, submissions = 0]
```

## 자바 병렬 스트림
## 병렬 스트림 사용시 주의점
