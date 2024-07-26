import NextAuth from "next-auth"
import CredentialsProvider from "next-auth/providers/credentials"
import { LoginSession, LoginVo } from './type';
import * as AdminApi from "@/api/admin"
import { JWT } from 'next-auth/jwt';


//后管平台登录
const AdminJwtProvider = CredentialsProvider({
  name: 'Admin',
  id: "Admin",
  credentials: {
    username: {},
    password: {},
    device: {}
  },
  async authorize(credentials, req): Promise<LoginVo | null> {
    const res = await AdminApi.login(credentials)
    const response = await res.json()
    if (res.ok && response?.token) {
      return {
        id: response.info.id,
        token: response.token,
        info: response.info,
        roles: response.roles,
        permissions: response.permissions,
        from: "Admin"
      }
    }
    return null
  }
})

const handler = NextAuth({
  providers: [AdminJwtProvider],
  session: {
    strategy: "jwt",
  },
  callbacks: {
    async signIn({ user, account, profile, email, credentials }) {
      return true
    },
    async redirect({ url, baseUrl }) {
      return baseUrl
    },
    async session(params: { session: LoginSession, user: LoginVo, token: JWT }) {
      const { session, user } = params
      session.token = user.token
      if (user.from == 'Admin') {
        session.info = user.info
        session.roles = user.roles
        session.permissions = user.permissions
      }
      return session
    },
    async jwt(params: { token: JWT, user: LoginVo }) {
      const { token, user } = params
      token.token = user.token
      return token
    }
  },
})

export { handler as GET, handler as POST }
