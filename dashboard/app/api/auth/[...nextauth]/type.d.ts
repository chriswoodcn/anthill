import type { Session, User } from "next-auth"
interface UserInfo {
  id: number,
  username: string
  nickname: string
  userType: string
  email?: string
  mobile?: string
  sex: string
  avatar?: string
  status: string
  comId?: string
  adminFlag: string
  createTime: number
  updateTime: number
}
interface LoginVo extends User {
  token?: string
  error?: string
  info?: UserInfo
  roles?: number[]
  permissions?: string[]
  from?: string
}
interface LoginSession extends Session {
  info?: UserInfo
  token?: string
  roles?: number[]
  permissions?: string[]
}
