import { NextFetchEvent, NextRequest, NextResponse } from "next/server";
import { MiddlewareFactory } from "./type";
import { withBasePath } from '..';
import configuration from "../../configuration.mjs"

export const withRoot: MiddlewareFactory = (next) => {
  return async (request: NextRequest, _next: NextFetchEvent) => {
    // 重定向首页
    if (/^\/$/.test(request.nextUrl.pathname)) {
      return NextResponse.redirect(new URL(withBasePath(configuration.RootPathAlias), request.url))
    }
    return next(request, _next);
  };
};