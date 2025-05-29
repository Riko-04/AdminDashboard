import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-term-of-services',
  templateUrl: './term-of-services.component.html',
  styleUrls: ['./term-of-services.component.css']
})
export class TermOfServicesComponent implements OnInit {
  effectiveDate: Date = new Date(); // Define the effectiveDate property
  accepted: boolean = false;
  acceptanceDate: Date | null = null;

  constructor(private router: Router) { }

  ngOnInit(): void {
    const accepted = localStorage.getItem('termsAccepted');
    if (accepted) {
      this.accepted = true;
      this.acceptanceDate = new Date(accepted);
    }
  }

  acceptTerms(): void {
    this.accepted = true;
    this.acceptanceDate = new Date();
    localStorage.setItem('termsAccepted', this.acceptanceDate.toString());
    this.router.navigate(['/home']);
  }
}