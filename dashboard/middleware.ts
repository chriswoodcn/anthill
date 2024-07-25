import { stackMiddlewares } from '@/lib/middleware/stackMiddlewares';
import { withRoot } from "@/lib/middleware/withRoot"
import { withLogger } from "@/lib/middleware/withLogger"

const middlewares = [withLogger, withRoot];

export default stackMiddlewares(middlewares);