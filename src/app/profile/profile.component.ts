import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  user: any = {};
  isUpdating: boolean = false;
  errorMessage: string = '';
  successMessage: string = '';
  
  constructor(private authService: AuthService, private router: Router,private userService: UserService) {}

  ngOnInit() {
    // Fetch user profile data from localStorage
    this.user = this.authService.getLoggedInUser();
    console.log('User data:', this.user); // Log the user data for debugging

  }

  updateUser() {
    if (!this.user?.sub) {
      this.errorMessage = 'No user ID available for update';
      return;
    }
  
    this.isUpdating = true;
    this.errorMessage = '';
    this.successMessage = '';
  
    const userToUpdate = {
      preferred_username: this.user.preferred_username,
      firstName: this.user.firstName,
      lastName: this.user.lastName,
      email: this.user.email,
      image: this.user.image,
      adresse: this.user.adresse,
      sex: this.user.sex,
      phone: this.user.phone,
      userId: this.user.sub
    };
  
    this.authService.updateUser(userToUpdate).subscribe({
      next: (response) => {
        console.log('Update response:', response); // This will be the string directly
        this.isUpdating = false;
        this.successMessage = response; // Use the plain text response
        
        // Update local user data
        const updatedUser = { ...this.user, ...userToUpdate };
        localStorage.setItem('user', JSON.stringify(updatedUser));
        
        setTimeout(() => this.successMessage = '', 3000);
      },
      error: (error) => {
        console.error('Failed to update user', error);
        this.isUpdating = false;
        
        // Handle different error types
        if (error.error instanceof ErrorEvent) {
          // Client-side error
          this.errorMessage = `Error: ${error.error.message}`;
        } else {
          // Backend error - might still be plain text
          this.errorMessage = error.error || error.message || 'Unknown error occurred';
        }
        
        setTimeout(() => this.errorMessage = '', 5000);
      }
    });
  }

  logout(): void {
    const userId = this.user?.sub;
    if (userId) {
      this.authService.logout(userId).subscribe({
        next: () => {
          localStorage.clear();
          this.router.navigate(['/login']);
        },
        error: (err) => {
          console.error('Logout failed:', err);
        }
      });
    } else {
      console.warn('No user ID found for logout');
    }
  }
}
