import { combineReducers, configureStore } from "@reduxjs/toolkit";
import settingConfigSlice from "@/store/settingConfigSlice";

const rootReducer = combineReducers({
  settingConfig: settingConfigSlice,
});

export default configureStore({
  reducer: rootReducer,
});

export type IRootState = ReturnType<typeof rootReducer>;
