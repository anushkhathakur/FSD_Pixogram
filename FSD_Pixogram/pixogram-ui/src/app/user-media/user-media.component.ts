import { Component, OnInit } from '@angular/core';
import { UploadFileService } from 'src/app/_services/upload-file.service';
import { HttpEventType, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TokenStorageService } from '../_services/token-storage.service';

@Component({
  selector: 'app-user-media',
  templateUrl: './user-media.component.html',
  styleUrls: ['./user-media.component.css']
})
export class UserMediaComponent implements OnInit {

  fileInfos: Observable<any>;
  userId = '';
  constructor(private uploadService: UploadFileService, private tokenStorage: TokenStorageService) { }

  ngOnInit() {
    this.userId = this.tokenStorage.getUser().id;
    this.fileInfos = this.uploadService.getFiles(this.userId);
  }

}
