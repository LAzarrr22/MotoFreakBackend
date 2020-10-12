import {Actions, Effect, ofType} from "@ngrx/effects";
import {Action, Store} from "@ngrx/store";
import {Router} from "@angular/router";
import {CommonComponentsService} from "../../../common/common.service";
import {Observable} from "rxjs";
import {MyProfileModel} from "../dto/response/my-profile.model";
import {
  GET_MY_PROFILE,
  GetMyProfileFail,
  GetMyProfileSuccess,
  MERGE_MY_ADDRESS,
  MERGE_MY_CONTACT,
  MERGE_MY_PROFILE,
  MergeMyAddress,
  MergeMyAddressFail,
  MergeMyAddressSuccess,
  MergeMyContact,
  MergeMyContactFail,
  MergeMyContactSuccess,
  MergeMyProfile,
  MergeMyProfileFail,
  MergeMyProfileSuccess
} from "../action/my-profile.action";
import {catchError, switchMap} from "rxjs/operators";
import {MyProfileApiService} from "../services/my-profile-api.service";
import {Injectable} from "@angular/core";


@Injectable()
export class MyProfileEffects {
  constructor(private actions$: Actions, private store$: Store
    , private router: Router, private myProfileService: MyProfileApiService
    , private errorService: CommonComponentsService) {
  }

  @Effect()
  getMyProfile$: Observable<Action> = this.actions$
    .pipe(ofType(GET_MY_PROFILE),
      switchMap(() => {
        return this.myProfileService.getMyProfile();
      }),
      switchMap((profileData: MyProfileModel) => [
        new GetMyProfileSuccess(profileData),
      ]),
      catchError((error, caught) => {
        this.store$.dispatch(new GetMyProfileFail(error.error.message));
        this.errorService.error(error);
        return caught;
      })
    );

  @Effect()
  mergeUser$: Observable<Action> = this.actions$
    .pipe(
      ofType(MERGE_MY_PROFILE),
      switchMap((action: MergeMyProfile) => {
        return this.myProfileService.mergeMyProfile(action.payload)
      }),
      switchMap((userData: string) => [
        new MergeMyProfileSuccess(userData),
      ]),
      catchError((error, caught) => {
        this.store$.dispatch(new MergeMyProfileFail(error.error));
        this.errorService.error(error);
        return caught;
      })
    );

  @Effect()
  mergeAddress$: Observable<Action> = this.actions$
    .pipe(
      ofType(MERGE_MY_ADDRESS),
      switchMap((action: MergeMyAddress) => {
        return this.myProfileService.mergeMyAddress(action.payload)
      }),
      switchMap((userData: string) => [
        new MergeMyAddressSuccess(userData),
      ]),
      catchError((error, caught) => {
        this.store$.dispatch(new MergeMyAddressFail(error.error));
        this.errorService.error(error);
        return caught;
      })
    );

  @Effect()
  mergeContact$: Observable<Action> = this.actions$
    .pipe(
      ofType(MERGE_MY_CONTACT),
      switchMap((action: MergeMyContact) => {
        return this.myProfileService.mergeMyContact(action.payload)
      }),
      switchMap((userData: string) => [
        new MergeMyContactSuccess(userData),
      ]),
      catchError((error, caught) => {
        this.store$.dispatch(new MergeMyContactFail(error.error));
        this.errorService.error(error);
        return caught;
      })
    );

}
