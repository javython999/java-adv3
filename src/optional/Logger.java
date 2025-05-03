package optional;

import java.util.function.Supplier;

public class Logger {

    private boolean isDebug = false;

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean debug) {
        isDebug = debug;
    }

   public void debug(Object message) {
        if (isDebug) {
           System.out.println("[DEBUG] " + message);
        }
   }

   // DEBUG로 설정한 경우 출력 - 람다를 받아서 실행
    public void debug(Supplier<?> supplier) {
        if (isDebug) {
            System.out.println("[DEBUG] " + supplier.get());
        }
    }
}
