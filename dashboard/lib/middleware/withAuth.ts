import { NextFetchEvent, NextRequest, NextResponse } from "next/server";
import { MiddlewareFactory } from "./type";
import { headers as getHeaders } from "next/headers";
import configuration from '@/configuration.mjs';
import { withBasePath } from '..';

export const withAuth: MiddlewareFactory = (next) => {
  return async (request: NextRequest, _next: NextFetchEvent) => {
    if (/^\/admin\//.test(request.nextUrl.pathname)) {
      if (getHeaders().has(configuration.AuthField)) {
        return next(request, _next);
      } else {
        return NextResponse.redirect(new URL(withBasePath(configuration.RootPathAlias), request.url))
      }
    }
    return next(request, _next);
  };
};