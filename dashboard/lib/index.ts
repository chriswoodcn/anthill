import configuration from "../configuration.mjs"
const basePath = configuration.BasePath
import CryptoJS from 'crypto-js';

export const withBasePath = (path: string) => path.startsWith("/") ? `${basePath}${path}` : `${basePath}/${path}`

const AES_KEY = CryptoJS.enc.Utf8.parse(configuration.AppName);
const AES_CipherOption = {
  mode: CryptoJS.mode.ECB,
  padding: CryptoJS.pad.Pkcs7
}
export const aesEncrypt = (data: string) => {
  return CryptoJS.AES.encrypt(data, AES_KEY, AES_CipherOption).toString();
}
export const aesDecrypt = (encrypted: string) => {
  const bytes = CryptoJS.AES.decrypt(encrypted, AES_KEY, AES_CipherOption);
  return bytes.toString(CryptoJS.enc.Utf8);
}
