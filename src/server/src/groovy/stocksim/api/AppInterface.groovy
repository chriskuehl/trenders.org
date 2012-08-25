package stocksim.api

class AppInterface {
    public static def codes = [
        OK: 200,
        
        SET_PASSWORD_FIRST: 401,
        BAD_LOGIN_INFO: 402,
        LOGIN_FIRST: 403,
        NOT_AVAILABLE: 404,
        UPGRADE_APP: 406,
        HIT_RATE_LIMIT: 429,
        
        SERVER_ERROR: 500
    ]
}