package ru.unlegit.dismath.relationship;

public interface PropertyCheckResult {

    PropertyCheckResult SUCCESS = new PropertyCheckResult() {
        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public String getCause() {
            throw new RuntimeException("mo cause for success result");
        }
    };

    static PropertyCheckResult fail(String cause) {
        return new PropertyCheckResult() {
            @Override
            public boolean isSuccess() {
                return false;
            }

            @Override
            public String getCause() {
                return cause;
            }
        };
    }

    boolean isSuccess();

    String getCause();
}