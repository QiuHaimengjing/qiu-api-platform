export declare namespace API {
  type BaseResponseBoolean_ = {
    /** 响应码 */
    code: number
    /** 响应数据 */
    data: boolean
    /** 响应描述 */
    description: string
    /** 响应消息 */
    message: string
  }

  type BaseResponseInterfaceListVO_ = {
    /** 响应码 */
    code: number
    data: InterfaceListVO
    /** 响应描述 */
    description: string
    /** 响应消息 */
    message: string
  }

  type BaseResponseInterfaceVO_ = {
    /** 响应码 */
    code: number
    data: InterfaceVO
    /** 响应描述 */
    description: string
    /** 响应消息 */
    message: string
  }

  type BaseResponseListStatisticsVO_ = {
    /** 响应码 */
    code: number
    /** 响应数据 */
    data: StatisticsVO[]
    /** 响应描述 */
    description: string
    /** 响应消息 */
    message: string
  }

  type BaseResponseLong_ = {
    /** 响应码 */
    code: number
    /** 响应数据 */
    data: number
    /** 响应描述 */
    description: string
    /** 响应消息 */
    message: string
  }

  type BaseResponseObject_ = {
    /** 响应码 */
    code: number
    /** 响应数据 */
    data: Record<string, any>
    /** 响应描述 */
    description: string
    /** 响应消息 */
    message: string
  }

  type BaseResponseString_ = {
    /** 响应码 */
    code: number
    /** 响应数据 */
    data: string
    /** 响应描述 */
    description: string
    /** 响应消息 */
    message: string
  }

  type BaseResponseUserInterfaceListVO_ = {
    /** 响应码 */
    code: number
    data: UserInterfaceListVO
    /** 响应描述 */
    description: string
    /** 响应消息 */
    message: string
  }

  type BaseResponseUserInterfaceVO_ = {
    /** 响应码 */
    code: number
    data: UserInterfaceVO
    /** 响应描述 */
    description: string
    /** 响应消息 */
    message: string
  }

  type BaseResponseUserKeyVO_ = {
    /** 响应码 */
    code: number
    data: UserKeyVO
    /** 响应描述 */
    description: string
    /** 响应消息 */
    message: string
  }

  type BaseResponseUserListVO_ = {
    /** 响应码 */
    code: number
    data: UserListVO
    /** 响应描述 */
    description: string
    /** 响应消息 */
    message: string
  }

  type BaseResponseUserVO_ = {
    /** 响应码 */
    code: number
    data: UserVO
    /** 响应描述 */
    description: string
    /** 响应消息 */
    message: string
  }

  type deleteUserInterfaceServiceParams = {
    /** id */
    id: number
  }

  type deleteUserServiceParams = {
    /** id */
    id: number
  }

  type getInterfaceByIdServiceParams = {
    /** id */
    id: number
  }

  type getUserInterfaceByIdServiceParams = {
    /** id */
    id: number
  }

  type getUserListServiceParams = {
    /** username */
    username?: string
  }

  type InterfaceAddRequest = {
    /** 描述 */
    description: string
    /** 服务器地址 */
    host: string
    /** 请求类型 */
    method: string
    /** 名称 */
    name: string
    /** 请求头 */
    requestHeader?: string
    /** 请求参数 */
    requestParams?: string
    /** 响应头 */
    responseHeader?: string
    /** 接口状态 （0-关闭，1-开启） */
    status?: number
    /** 接口地址 */
    url: string
  }

  type InterfaceInvokeRequest = {
    /** 接口id */
    id: number
    /** 请求参数 */
    requestParams?: string
  }

  type InterfaceListVO = {
    /** 脱敏的接口列表 */
    interfaceList?: InterfaceVO[]
    /** 总记录数 */
    total?: number
  }

  type InterfaceQueryRequest = {
    /** 描述 */
    description?: string
    /** 服务器地址 */
    host?: string
    /** 接口id */
    id?: number
    /** 名称 */
    name?: string
    /** 页码 */
    pageNO: number
    /** 每页记录数 */
    pageSize: number
    /** 接口状态 （0-关闭，1-开启） */
    status?: number
    /** 接口地址 */
    url?: string
  }

  type interfaceStatisticServiceParams = {
    /** limit */
    limit: number
  }

  type InterfaceUpdateRequest = {
    /** 描述 */
    description?: string
    /** 服务器地址 */
    host?: string
    /** 接口id */
    id: number
    /** 请求类型 */
    method?: string
    /** 名称 */
    name?: string
    /** 请求头 */
    requestHeader?: string
    /** 请求参数 */
    requestParams?: string
    /** 响应头 */
    responseHeader?: string
    /** 接口状态 （0-关闭，1-开启） */
    status?: number
    /** 接口地址 */
    url?: string
  }

  type InterfaceVO = {
    /** 创建时间 */
    createTime?: string
    /** 描述 */
    description?: string
    /** 服务器地址 */
    host?: string
    /** 接口id */
    id?: number
    /** 请求类型 */
    method?: string
    /** 名称 */
    name?: string
    /** 请求头 */
    requestHeader?: string
    /** 请求参数 */
    requestParams?: string
    /** 响应头 */
    responseHeader?: string
    /** 接口状态 （0-关闭，1-开启） */
    status?: number
    /** 更新时间 */
    updateTime?: string
    /** 接口地址 */
    url?: string
    /** 创建人 */
    userId?: number
  }

  type offlineInterfaceServiceParams = {
    /** id */
    id: number
  }

  type onlineInterfaceServiceParams = {
    /** id */
    id: number
  }

  type StatisticsVO = {
    /** 接口id */
    id?: number
    /** 名称 */
    name?: string
    /** 调用次数 */
    totalNum?: string
  }

  type User = {
    /** 用户唯一标识 */
    accessKey?: string
    /** 头像 */
    avatarUrl?: string
    /** 创建时间 */
    createTime?: string
    /** 是否删除 0 1（默认值0） */
    deleted?: number
    /** 邮箱 */
    email?: string
    /** 性别 */
    gender?: number
    /** 用户id */
    id?: number
    /** 电话 */
    phone?: string
    /** 个人简介 */
    profile?: string
    /** 加密盐 */
    salt?: string
    /** 用户密钥 */
    secretKey?: string
    /** 标签列表 */
    tags?: string
    /** 更新时间 */
    updateTime?: string
    /** 账号 */
    userAccount?: string
    /** 密码 */
    userPassword?: string
    /** 角色 0-普通用户 1-管理员 */
    userRole?: number
    /** 状态，0正常 */
    userStatus?: number
    /** 昵称 */
    username?: string
  }

  type UserInterfaceAddRequest = {
    /** 接口id */
    interfaceId: number
    /** 用户id */
    userId: number
  }

  type UserInterfaceListVO = {
    /** 总记录数 */
    total?: number
    /** 脱敏的用户接口列表 */
    userInterfaceList?: UserInterfaceVO[]
  }

  type UserInterfaceQueryRequest = {
    /** id */
    id?: number
    /** 接口id */
    interfaceId?: number
    /** 页码 */
    pageNO: number
    /** 每页记录数 */
    pageSize: number
    /** 调用状态 （0-正常，1-禁用） */
    status?: number
    /** 用户id */
    userId?: number
  }

  type UserInterfaceUpdateRequest = {
    /** id */
    id: number
    /** 接口id */
    interfaceId?: number
    /** 剩余调用次数 */
    leftNum?: number
    /** 调用状态 （0-正常，1-禁用） */
    status?: number
    /** 总调用次数 */
    totalNum?: number
    /** 用户id */
    userId?: number
  }

  type UserInterfaceVO = {
    /** 创建时间 */
    createTime?: string
    /** id */
    id?: number
    /** 接口id */
    interfaceId?: number
    /** 剩余调用次数 */
    leftNum?: number
    /** 调用状态 （0-正常，1-禁用） */
    status?: number
    /** 总调用次数 */
    totalNum?: number
    /** 更新时间 */
    updateTime?: string
    /** 用户id */
    userId?: number
  }

  type UserKeyVO = {
    /** 用户唯一标识 */
    accessKey?: string
    /** 用户id */
    id?: number
    /** 用户密钥 */
    secretKey?: string
  }

  type UserListVO = {
    /** 总记录数 */
    total?: number
    /** 脱敏的用户列表 */
    userList?: UserVO[]
  }

  type UserLoginRequest = {
    /** 账号 */
    userAccount: string
    /** 密码 */
    userPassword: string
  }

  type UserRegisterRequest = {
    /** 账号 */
    userAccount: string
    /** 密码 */
    userPassword: string
    /** 校验密码 */
    verifyPassword: string
  }

  type UserUpdateRequest = {
    /** 头像 */
    avatarUrl?: string
    /** 邮箱 */
    email?: string
    /** 性别 */
    gender?: number
    /** 用户id */
    id: number
    /** 电话 */
    phone?: string
    /** 个人简介 */
    profile?: string
    /** 标签列表 */
    tags?: string
    /** 昵称 */
    username?: string
  }

  type UserVO = {
    /** 头像 */
    avatarUrl?: string
    /** 创建时间 */
    createTime?: string
    /** 邮箱 */
    email?: string
    /** 性别 */
    gender?: number
    /** 用户id */
    id?: number
    /** 电话 */
    phone?: string
    /** 个人简介 */
    profile?: string
    /** 标签列表 */
    tags?: string
    /** 更新时间 */
    updateTime?: string
    /** 账号 */
    userAccount?: string
    /** 角色 0-普通用户 1-管理员 */
    userRole?: number
    /** 状态，0正常 */
    userStatus?: number
    /** 昵称 */
    username?: string
  }
}
