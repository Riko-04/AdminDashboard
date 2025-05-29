import { Component } from '@angular/core';

@Component({
  selector: 'app-cardsection',
  templateUrl: './cardsection.component.html',
  styleUrl: './cardsection.component.css'
})

export class CardsectionComponent {
  features = [
    {
      icon: 'fas fa-users',
      title: 'Manage Users',
      description: 'Admins can view, edit, and manage user profiles securely.'
    },
    {
      icon: 'fas fa-layer-group',
      title: 'User Groups',
      description: 'Manage and organize users into logical groups easily.'
    },
    {
      icon: 'fas fa-user-plus',
      title: 'Easy Onboarding',
      description: 'Customers can join and create groups in just a few steps.'
    }
  ];
}
