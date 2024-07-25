import configuration from "../configuration.mjs"
const basePath = configuration.BasePath

export const withBasePath = (path: string) => path.startsWith("/") ? `${basePath}${path}` : `${basePath}/${path}`
