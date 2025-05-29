import { Component } from '@angular/core';

@Component({
  selector: 'app-contact-us',
  templateUrl: './contact-us.component.html',
  styleUrl: './contact-us.component.css'
})
export class ContactUsComponent {
  user = {
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    message: ''
  };
  constructor() {
  
}
  onSubmit() {
    console.log('User Registered:', this.user);
    alert('Submitted successfully!');
  }
  ngOnInit(): void {
      // Initialize the user object or perform any other setup here
      this.user = {
        firstName: '',
        lastName: '',
        email: '',
        phone: '',
        message: ''
      };
  }
}
