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

