import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../../../environments/environment";
import {Observable} from "rxjs";

@Injectable()
export class CarsApiService {

  constructor(private readonly httpClient: HttpClient) {
  }

  getCompanies(): Observable<string[]> {
    return this.httpClient.get<string[]>(`${environment.apiUrl}/cars/all/companies`)
  }

  getModels(company: string): Observable<string[]> {
    return this.httpClient.get<string[]>(`${environment.apiUrl}/cars/all/models/${company}`)
  }

  getGenerations(company: string, model: string): Observable<string[]> {
    return this.httpClient.get<string[]>(`${environment.apiUrl}/cars/all/generations/${company}/${model}`)
  }
}
