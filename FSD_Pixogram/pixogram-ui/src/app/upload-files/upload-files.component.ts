import { Component, OnInit } from '@angular/core';
import { UploadFileService } from 'src/app/_services/upload-file.service';
import { HttpEventType, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TokenStorageService } from '../_services/token-storage.service';

@Component({
  selector: 'app-upload-files',
  templateUrl: './upload-files.component.html',
  styleUrls: ['./upload-files.component.css']
})
export class UploadFilesComponent implements OnInit {
  form: any = {};
  selectedFiles: FileList;
  currentFile: File;
  message = '';
  userId = '';
 
  fileInfos: Observable<any>;
  constructor(private uploadService: UploadFileService, private tokenStorage: TokenStorageService) { }

  ngOnInit() {
    this.userId = this.tokenStorage.getUser().id;
    this.fileInfos = this.uploadService.getFiles(this.userId);
  }

  selectFile(event) {
    this.selectedFiles = event.target.files;
    this.form.file = event.target.files[0];
    this.form.fileName = event.target.files[0].name;
    this.form.fileType = event.target.files[0].type;
  }

  upload() {
    this.form.userId = this.userId;
      this.currentFile = this.selectedFiles.item(0);
     this.uploadService.upload(this.form, this.currentFile).subscribe(
      event => {
        if (event instanceof HttpResponse) {
          this.message = event.body.message;
          this.fileInfos = this.uploadService.getFiles(this.userId);
        }
      },
      err => {
        this.message = 'Could not upload the file!';
        this.currentFile = undefined;
      });
    this.selectedFiles = undefined;
  }
}
