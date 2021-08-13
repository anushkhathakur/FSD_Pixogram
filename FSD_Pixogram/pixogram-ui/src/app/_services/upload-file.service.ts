import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpHeaders, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';

const AUTH_API = 'http://localhost:9001';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};
@Injectable({
  providedIn: 'root'
})
export class UploadFileService {

  private baseUrl = 'http://localhost:9001/producer/media';

  constructor(private http: HttpClient) { }

  upload(singleMediaRequest, file): Observable<HttpEvent<any>> {
    const formData: FormData = new FormData();
  formData.append('file', file);
      formData.append('singleMediaRequest', new Blob([JSON.stringify({
      "userId": singleMediaRequest.userId,
      "mediaTitle": singleMediaRequest.mediaTitle,
      "desc": singleMediaRequest.desc,
      "fileName": singleMediaRequest.fileName
    })], {
      type: "application/json"
    }));

    const req = new HttpRequest('POST', `${this.baseUrl}/singleFileUpload`, formData, {
      reportProgress: true,
      responseType: 'json',
    });

    return this.http.request(req);
  }

  getFiles(userId): Observable<any> {
    return this.http.get(`${this.baseUrl}/userfiles/`+userId);
  }
}
