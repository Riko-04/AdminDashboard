import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-privacy-policy',
  templateUrl: './privacy-policy.component.html',
  styleUrls: ['./privacy-policy.component.css']
})
export class PrivacyPolicyComponent implements OnInit {
  lastUpdated = '2025-05-15'; // or any other default value
  accepted: boolean = false;
  acceptanceDate: Date | null = null;

  constructor(private router: Router) { }

  ngOnInit(): void {
    const accepted = localStorage.getItem('privacyPolicyAccepted');
    if (accepted) {
      this.accepted = true;
      this.acceptanceDate = new Date(accepted);
    }
  }

  acceptPolicy(): void {
    this.accepted = true;
    this.acceptanceDate = new Date();
    localStorage.setItem('privacyPolicyAccepted', this.acceptanceDate.toString());
    this.router.navigate(['/home']);
  }
}