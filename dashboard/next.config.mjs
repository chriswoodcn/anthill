/** @type {import('next').NextConfig} */
import configuration from "./configuration.mjs";
const nextConfig = {
  basePath: configuration.BasePath,
  swcMinify: true,
  eslint: {
    ignoreDuringBuilds: true,
  },
  logging: {
    fetches: {
      fullUrl: true,
    },
  },
  experimental: {
    //support app router tree shaking
    optimizePackageImports: ["@mantine/core", "@mantine/hooks"],
  },
};

export default nextConfig;
