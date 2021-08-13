import { Component, OnInit } from '@angular/core';
import { UserService } from '../_services/user.service';
import { TokenStorageService } from '../_services/token-storage.service';
import { Observable } from 'rxjs';
import { UploadFileService } from '../_services/upload-file.service';
@Component({
  selector: 'app-board-user',
  templateUrl: './board-user.component.html',
  styleUrls: ['./board-user.component.css'],
})
export class BoardUserComponent implements OnInit {
  content = '';
  mediaContent=''
  currentUser: any;
  userId = '';

  fileInfos: Observable<any>;
  constructor(private userService: UserService,private token: TokenStorageService,private uploadService: UploadFileService) { }

  ngOnInit() {
    this.userService.getUserBoard().subscribe(
      data => {
        this.content = data;
        this.currentUser = this.token.getUser();
        this.mediaContent="welcome here..";
        this.userId = this.token.getUser().id;
        this.fileInfos = this.uploadService.getFiles(this.userId);
      },
      err => {
        this.content = JSON.parse(err.error).message;
      }
    );
  }
  
}
