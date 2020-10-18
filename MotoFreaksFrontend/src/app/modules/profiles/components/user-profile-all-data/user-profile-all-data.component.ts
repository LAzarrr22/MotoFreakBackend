import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {UserModel} from "../../../users/logic/dto/response/user-model";
import {UsersService} from "../../../users/logic/services/users.service";
import {RolesEnum} from "../../../authentication/logic/enums/roles.enum";
import {AuthService} from "../../../authentication/logic/services/auth.service";

@Component({
  selector: 'app-user-profile-all-data',
  templateUrl: './user-profile-all-data.component.html',
  styleUrls: ['./user-profile-all-data.component.scss']
})
export class UserProfileAllDataComponent implements OnInit {

  @Input()
  userProfile: UserModel;
  @Input()
  currentRoles: RolesEnum[];

  @Output()
  reloadUser = new EventEmitter();

  constructor(private usersService: UsersService, private authService: AuthService) {
  }

  ngOnInit(): void {
  }

  addFriend(id: string) {
    this.usersService.addFriend(id);
    this.reloadUser.emit();
  }

  setAdmin() {
    this.authService.setAdmin(this.userProfile.id);
    this.reloadUser.emit();
  }

  setModerator() {
    this.authService.setModerator(this.userProfile.id);
    this.reloadUser.emit();
  }

}
