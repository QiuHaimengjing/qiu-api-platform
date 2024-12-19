enum ResponseCode {
  SUCCESS = 20000,
  PARAMS_ERROR = 40000,
  NULL_ERROR = 40001,
  NOT_LOGIN = 40100,
  NO_AUTH = 40300,
  FORBIDDEN = 40301,
  SYSTEM_ERROR = 50000
}

export default ResponseCode
